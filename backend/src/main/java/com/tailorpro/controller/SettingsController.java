package com.tailorpro.controller;
import com.tailorpro.model.ShopSettings;
import com.tailorpro.repository.ShopSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/settings") @RequiredArgsConstructor
public class SettingsController {
    private final ShopSettingsRepository repo;
    @GetMapping public ShopSettings get(){ return repo.findById(1L).orElseThrow(() -> new RuntimeException("Settings missing")); }
    @PutMapping public ShopSettings save(@RequestBody ShopSettings s){ s.setId(1L); return repo.save(s); }
}
