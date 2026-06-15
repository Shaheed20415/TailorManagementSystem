package com.tailorpro.controller;
import com.tailorpro.model.*;
import com.tailorpro.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/customers") @RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customers;
    private final MeasurementRepository measurements;

    @GetMapping public List<Customer> all(@RequestParam(defaultValue="") String q){ return q.isBlank() ? customers.findAll() : customers.findByNameContainingIgnoreCaseOrMobileContaining(q, q); }
    @GetMapping("/{id}") public Customer one(@PathVariable Long id){ return customers.findById(id).orElseThrow(() -> new RuntimeException("Customer not found")); }
    @PostMapping public Customer create(@Valid @RequestBody Customer c){ c.setId(null); return customers.save(c); }
    @PutMapping("/{id}") public Customer update(@PathVariable Long id, @Valid @RequestBody Customer input){
        Customer c = customers.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        c.setName(input.getName()); c.setMobile(input.getMobile()); c.setAddress(input.getAddress());
        return customers.save(c);
    }
    @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id){ customers.deleteById(id); return Map.of("message","Customer deleted"); }

    @PostMapping("/{id}/measurements") public Measurement addMeasurement(@PathVariable Long id, @RequestBody Measurement m){
        Customer c = customers.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        m.setId(null); m.setCustomer(c); return measurements.save(m);
    }
    @GetMapping("/{id}/measurements") public List<Measurement> customerMeasurements(@PathVariable Long id){ return measurements.findByCustomerIdOrderByCreatedAtDesc(id); }
}
