package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.CreateUserRequest;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.AuthResponse;
import com.example.userservice.dto.response.UserProfileResponse;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserRepository userRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public Optional<UserDto> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Update login info
                user.setLastLoginAt(LocalDateTime.now());
                user.setLoginCount(user.getLoginCount() + 1);
                userRepository.save(user);
                return Optional.of(new UserDto(user));
            }
        }
        return Optional.empty();
    }
    
    public AuthResponse register(CreateUserRequest request) {
        // Create user through UserService
        UserDto userDto = userService.createUser(request.getEmail(), request.getName(), request.getPassword());
        
        // Generate tokens
        String accessToken = jwtService.generateAccessToken(userDto.getEmail());
        String refreshToken = jwtService.generateRefreshToken(userDto.getEmail());
        
        // Get user entity for profile response
        User user = userService.getUserEntityById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found after creation"));
        
        UserProfileResponse profile = new UserProfileResponse(user);
        
        return new AuthResponse(accessToken, refreshToken, profile, jwtService.getExpirationTime());
    }
    
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Optional<UserDto> userOpt = authenticateUser(request.getEmail(), request.getPassword());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }
        
        UserDto userDto = userOpt.get();
        
        // Generate tokens
        String accessToken = jwtService.generateAccessToken(userDto.getEmail());
        String refreshToken = jwtService.generateRefreshToken(userDto.getEmail());
        
        // Get user entity for profile response
        User user = userService.getUserEntityById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfileResponse profile = new UserProfileResponse(user);
        
        return new AuthResponse(accessToken, refreshToken, profile, jwtService.getExpirationTime());
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        // Extract username from refresh token
        String username = jwtService.extractUsername(refreshToken);
        
        // Validate refresh token
        Optional<UserDto> userOpt = userService.getUserByEmail(username);
        if (userOpt.isEmpty() || !jwtService.validateToken(refreshToken, username)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        UserDto userDto = userOpt.get();
        
        // Generate new access token
        String newAccessToken = jwtService.generateAccessToken(userDto.getEmail());
        
        // Get user entity for profile response
        User user = userService.getUserEntityById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfileResponse profile = new UserProfileResponse(user);
        
        // Return response with same refresh token
        return new AuthResponse(newAccessToken, refreshToken, profile, jwtService.getExpirationTime());
    }
    
    public void logout(String token) {
        // In a real implementation, you might want to:
        // 1. Add the token to a blacklist
        // 2. Remove refresh token from database
        // 3. Clear any server-side sessions
        
        // For now, logout is handled client-side by removing tokens
    }
}