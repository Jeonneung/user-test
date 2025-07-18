package com.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleOAuthConfig {
    
    @Value("${GOOGLE_CLIENT_ID:}")
    private String clientId;
    
    @Value("${GOOGLE_CLIENT_SECRET:}")
    private String clientSecret;
    
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
}