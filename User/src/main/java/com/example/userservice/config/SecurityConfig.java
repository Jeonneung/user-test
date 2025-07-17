package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .antMatchers("/api/health", "/api/users", "/api/auth/login").permitAll()
                .antMatchers("/login/oauth2/**", "/oauth2/**").permitAll()
                .antMatchers("/api/auth/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google")
                .defaultSuccessUrl("/api/auth/oauth2/success", true)
                .failureUrl("/api/auth/oauth2/failure")
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorization")
                    .and()
                .redirectionEndpoint()
                    .baseUri("/login/oauth2/code/*")
            );
        
        return http.build();
    }
}