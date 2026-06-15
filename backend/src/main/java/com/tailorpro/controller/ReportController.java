package com.tailorpro.controller;
import com.tailorpro.model.*;
import com.tailorpro.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@RestController @RequestMapping("/api/reports") @RequiredArgsConstructor
public class ReportController {
    private final OrderRepository orders;
    @GetMapping("/monthly") public Map<String,Object> monthly(@RequestParam(defaultValue="0") int monthOffset){
        LocalDate base = LocalDate.now().plusMonths(monthOffset);
        LocalDate start = base.withDayOfMonth(1), end = base.withDayOfMonth(base.lengthOfMonth());
        var list = orders.findByOrderDateBetween(start,end);
        BigDecimal revenue = list.stream().filter(o -> o.getStatus()!=OrderStatus.CANCELLED).map(TailorOrder::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Long> status = list.stream().collect(Collectors.groupingBy(o -> o.getStatus().name(), Collectors.counting()));
        Map<String, Long> dress = list.stream().collect(Collectors.groupingBy(o -> o.getDressType().name(), Collectors.counting()));
        return Map.of("month", base.getMonth().name()+" "+base.getYear(), "orders", list.size(), "revenue", revenue, "status", status, "dressTypes", dress);
    }
}
