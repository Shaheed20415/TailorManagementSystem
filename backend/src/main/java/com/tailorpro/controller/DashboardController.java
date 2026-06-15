package com.tailorpro.controller;
import com.tailorpro.dto.DashboardResponse;
import com.tailorpro.model.*;
import com.tailorpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.*;

@RestController @RequestMapping("/api/dashboard") @RequiredArgsConstructor
public class DashboardController {
    private final CustomerRepository customers;
    private final OrderRepository orders;

    @GetMapping public DashboardResponse dashboard(){
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        var monthOrders = orders.findByOrderDateBetween(start, end);
        BigDecimal revenue = monthOrders.stream().filter(o -> o.getStatus()!=OrderStatus.CANCELLED).map(TailorOrder::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DashboardResponse(customers.count(), orders.count(), orders.countByStatus(OrderStatus.PENDING), orders.countByStatus(OrderStatus.READY), orders.countByStatus(OrderStatus.DELIVERED), orders.findByDeliveryDate(now).size(), monthOrders.size(), revenue);
    }
}
