package com.example.gateway.dto.response;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    
    public UserResponse() {}
    
    public UserResponse(Long id, String email, String name, String role, Boolean emailVerified, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
    }
    
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
    
    public Boolean getEmailVerified() { 
        return emailVerified; 
    }
    
    public void setEmailVerified(Boolean emailVerified) { 
        this.emailVerified = emailVerified; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}