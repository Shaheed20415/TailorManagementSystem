package com.tailorpro.dto;
import com.tailorpro.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record OrderRequest(Long customerId, Long measurementId, DressType dressType, Integer quantity, BigDecimal amount, BigDecimal advance, PaymentMode paymentMode, LocalDate deliveryDate, String specialNotes) {}
