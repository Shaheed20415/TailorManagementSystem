package com.tailorpro.config;

import com.tailorpro.model.*;
import com.tailorpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.time.*;

@Configuration @RequiredArgsConstructor
public class DataSeeder {
    private final UserAccountRepository users;
    private final CustomerRepository customers;
    private final OrderRepository orders;
    private final ShopSettingsRepository settings;

    @Bean CommandLineRunner seed(){ return args -> {
        users.findByUsername("admin").orElseGet(() -> users.save(UserAccount.builder().username("admin").password("admin123").createdAt(LocalDateTime.now()).build()));
        if(settings.findById(1L).isEmpty()) settings.save(ShopSettings.builder().id(1L).shopName("Tailor Pro Studio").ownerName("Owner").phone("9999999999").address("Main Road").invoicePrefix("TP").defaultDiscount(BigDecimal.ZERO).invoiceTerms("Thank you. Goods once delivered cannot be returned after alteration approval.").build());
        if(customers.count()==0){
            Customer c = Customer.builder().name("Sample Customer").mobile("9876543210").address("Kalikiri").build();
            customers.save(c);
            Measurement m = Measurement.builder().customer(c).dressType(DressType.SHIRT).chest(40.0).waist(36.0).shoulder(18.0).sleeve(24.0).length(29.0).neck(15.0).notes("Regular fit").build();
            c.getMeasurements().add(m);
            customers.save(c);
            TailorOrder o = TailorOrder.builder().customer(c).orderCode("TP-1001").dressType(DressType.SHIRT).quantity(2).amount(new BigDecimal("1200")).advance(new BigDecimal("500")).balance(new BigDecimal("700")).paymentMode(PaymentMode.CASH).status(OrderStatus.PENDING).orderDate(LocalDate.now()).deliveryDate(LocalDate.now().plusDays(2)).measurementId(m.getId()).measurementSnapshot("Chest 40, Waist 36, Shoulder 18, Sleeve 24, Length 29").specialNotes("White buttons").build();
            orders.save(o);
        }
    }; }
}
