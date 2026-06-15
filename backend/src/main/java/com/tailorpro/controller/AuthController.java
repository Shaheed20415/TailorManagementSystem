package com.tailorpro.controller;
import com.tailorpro.dto.AuthDtos.*;
import com.tailorpro.model.UserAccount;
import com.tailorpro.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final UserAccountRepository users;
    @PostMapping("/login") public LoginResponse login(@RequestBody LoginRequest req){
        UserAccount u = users.findByUsername(req.username()).orElseThrow(() -> new RuntimeException("Invalid username"));
        if(!u.getPassword().equals(req.password())) throw new RuntimeException("Invalid password");
        return new LoginResponse(UUID.randomUUID().toString(), u.getUsername());
    }
    @PostMapping("/forgot-password") public Map<String,String> reset(@RequestBody ResetPasswordRequest req){
        UserAccount u = users.findByUsername(req.username()).orElseThrow(() -> new RuntimeException("Username not found"));
        if(req.newPassword()==null || req.newPassword().length()<6) throw new RuntimeException("Password must be minimum 6 characters");
        u.setPassword(req.newPassword()); users.save(u);
        return Map.of("message", "Password reset successful. Please login again.");
    }
}
