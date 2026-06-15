package com.tailorpro.controller;
import com.tailorpro.dto.OrderRequest;
import com.tailorpro.model.*;
import com.tailorpro.repository.OrderRepository;
import com.tailorpro.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/orders") @RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orders;
    private final OrderService service;

    @GetMapping public List<TailorOrder> all(@RequestParam(defaultValue="") String q, @RequestParam(required=false) OrderStatus status){ return orders.searchOrders(q, status); }
    @GetMapping("/work-queue") public List<TailorOrder> queue(){ return orders.workQueue(); }
    @GetMapping("/{id}") public TailorOrder one(@PathVariable Long id){ return orders.findById(id).orElseThrow(() -> new RuntimeException("Order not found")); }
    @PostMapping public TailorOrder create(@RequestBody OrderRequest req){ return service.create(req); }
    @PutMapping("/{id}") public TailorOrder update(@PathVariable Long id, @RequestBody OrderRequest req){ return service.update(id, req); }
    @PatchMapping("/{id}/status") public TailorOrder status(@PathVariable Long id, @RequestParam OrderStatus status){ return service.status(id, status); }
    @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id){ orders.deleteById(id); return Map.of("message","Order deleted"); }
}
