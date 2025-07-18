package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.response.AuthResponse;
import com.example.userservice.dto.response.UserProfileResponse;
import com.example.userservice.model.Provider;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2Controller {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;

    @GetMapping("/success")
    public String oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User, 
                               HttpServletResponse response) {
        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            if (email == null || name == null) {
                return "redirect:http://localhost:3000/auth/error?message=" + 
                       URLEncoder.encode("Missing required user information", StandardCharsets.UTF_8);
            }

            // 기존 사용자 확인 또는 새 사용자 생성
            Optional<UserDto> existingUser = userService.getUserByEmail(email);
            UserDto user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                userService.updateLoginInfo(user.getId());
            } else {
                user = userService.createOAuth2User(email, name, Provider.GOOGLE);
            }

            // 실제 JWT 토큰 생성
            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);
            
            // HttpOnly 쿠키로 토큰 설정
            setTokenCookies(response, accessToken, refreshToken);
            
            // 토큰 없이 안전하게 리다이렉트
            return "redirect:http://localhost:3000/auth/success";

        } catch (Exception e) {
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:http://localhost:3000/auth/error?message=" + encodedMessage;
        }
    }
    
    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // Access Token 쿠키 설정
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)                    // XSS 공격 방지
                .secure(false)                     // HTTPS에서만 전송 (개발환경에서는 false)
                .sameSite("Lax")                   // CSRF 공격 방지
                .path("/")                         // 모든 경로에서 접근 가능
                .maxAge(60 * 60)                   // 1시간 (JWT access token과 동일)
                .build();
        
        // Refresh Token 쿠키 설정
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)                    // XSS 공격 방지
                .secure(false)                     // HTTPS에서만 전송 (개발환경에서는 false)
                .sameSite("Lax")                   // CSRF 공격 방지
                .path("/api/auth/refresh")         // refresh 엔드포인트에서만 접근
                .maxAge(30 * 24 * 60 * 60)         // 30일 (JWT refresh token과 동일)
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    @GetMapping("/failure")
    public String oauth2Failure() {
        String encodedMessage = URLEncoder.encode("OAuth2 authentication failed", StandardCharsets.UTF_8);
        return "redirect:http://localhost:3000/auth/error?message=" + encodedMessage;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not authenticated"));
        }

        try {
            String email = oauth2User.getAttribute("email");
            Optional<UserDto> userOpt = userService.getUserByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userService.getUserEntityById(userOpt.get().getId()).get();
                UserProfileResponse profile = new UserProfileResponse(user);
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // OAuth2 성공 후 실제 인증 정보를 반환하는 API 엔드포인트
    @GetMapping("/callback")
    public ResponseEntity<?> oauth2Callback(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not authenticated"));
        }

        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            if (email == null || name == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required user information"));
            }

            // 기존 사용자 확인 또는 새 사용자 생성
            Optional<UserDto> existingUser = userService.getUserByEmail(email);
            UserDto user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                userService.updateLoginInfo(user.getId());
            } else {
                user = userService.createOAuth2User(email, name, Provider.GOOGLE);
            }

            // JWT 토큰 생성
            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);
            
            // 사용자 프로필 정보
            User userEntity = userService.getUserEntityById(user.getId()).get();
            UserProfileResponse profile = new UserProfileResponse(userEntity);
            
            // AuthResponse 형태로 반환
            AuthResponse response = new AuthResponse(accessToken, refreshToken, profile, jwtService.getExpirationTime());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}