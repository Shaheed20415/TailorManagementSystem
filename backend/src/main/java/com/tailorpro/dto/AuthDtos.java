package com.tailorpro.dto;
public class AuthDtos {
    public record LoginRequest(String username, String password) {}
    public record ResetPasswordRequest(String username, String newPassword) {}
    public record LoginResponse(String token, String username) {}
}
