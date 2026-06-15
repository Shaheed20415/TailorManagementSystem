package com.tailorpro.controller;
import com.tailorpro.model.*;
import com.tailorpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/invoices") @RequiredArgsConstructor
public class InvoiceController {
    private final OrderRepository orders;
    private final ShopSettingsRepository settings;
    @GetMapping("/{orderId}") public Map<String,Object> invoice(@PathVariable Long orderId){
        TailorOrder o = orders.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        ShopSettings s = settings.findById(1L).orElseThrow(() -> new RuntimeException("Settings missing"));
        return Map.of("shop", s, "order", o, "invoiceNo", s.getInvoicePrefix()+"-INV-"+o.getId());
    }
}
