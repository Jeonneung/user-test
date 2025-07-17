package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.Provider;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2Controller {

    @Autowired
    private UserService userService;

    @GetMapping("/success")
    public String oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

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
            String token = "oauth2-jwt-token-" + System.currentTimeMillis();
            
            // 프론트엔드로 리디렉션하면서 토큰과 사용자 정보 전달 (임시로 간단한 성공 페이지)
            return "redirect:https://oauth.buildingbite.com/api/auth/oauth2/user?token=" + token;

        } catch (Exception e) {
            return "redirect:http://localhost:3000/auth/error?message=" + e.getMessage();
        }
    }

    @GetMapping("/failure")
    public String oauth2Failure() {
        return "redirect:http://localhost:3000/auth/error?message=OAuth2%20authentication%20failed";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not authenticated"));
        }

        return ResponseEntity.ok(Map.of(
            "email", oauth2User.getAttribute("email"),
            "name", oauth2User.getAttribute("name"),
            "picture", oauth2User.getAttribute("picture")
        ));
    }
}