package com.inventory.billing.dto;

import java.util.List;

public class LoginResponse {
    private String token;
    private String username;
    private String fullName;
    private List<String> roles;

    public LoginResponse() {}

    public LoginResponse(String token, String username, String fullName, List<String> roles) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.roles = roles;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private String token;
        private String username;
        private String fullName;
        private List<String> roles;

        public LoginResponseBuilder token(String token) { this.token = token; return this; }
        public LoginResponseBuilder username(String username) { this.username = username; return this; }
        public LoginResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public LoginResponseBuilder roles(List<String> roles) { this.roles = roles; return this; }
        public LoginResponse build() { return new LoginResponse(token, username, fullName, roles); }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
