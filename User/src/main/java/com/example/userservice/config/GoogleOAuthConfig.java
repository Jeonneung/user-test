package com.example.userservice.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleOAuthConfig {
    
    private String clientId;
    private String clientSecret;
    
    @PostConstruct
    public void loadGoogleCredentials() {
        try {
            ClassPathResource resource = new ClassPathResource("client-secret.json");
            if (resource.exists()) {
                InputStream inputStream = resource.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(inputStream);
                
                JsonNode webNode = rootNode.get("web");
                if (webNode != null) {
                    this.clientId = webNode.get("client_id").asText();
                    this.clientSecret = webNode.get("client_secret").asText();
                    
                    // 환경 변수로 설정하여 Spring Security OAuth2가 사용할 수 있도록 함
                    System.setProperty("GOOGLE_CLIENT_ID", this.clientId);
                    System.setProperty("GOOGLE_CLIENT_SECRET", this.clientSecret);
                    
                    System.out.println("Google OAuth credentials loaded successfully");
                }
            } else {
                System.out.println("client-secret.json not found. Using default configuration.");
            }
        } catch (IOException e) {
            System.err.println("Failed to load Google OAuth credentials: " + e.getMessage());
        }
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
}