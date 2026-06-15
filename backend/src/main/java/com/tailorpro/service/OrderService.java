package com.tailorpro.service;

import com.tailorpro.dto.OrderRequest;
import com.tailorpro.model.*;
import com.tailorpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service @RequiredArgsConstructor
public class OrderService {
    private final CustomerRepository customers;
    private final MeasurementRepository measurements;
    private final OrderRepository orders;

    public TailorOrder create(OrderRequest req){
        Customer c = customers.findById(req.customerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        Measurement m = resolveMeasurement(req.measurementId(), c.getId(), req.dressType());
        BigDecimal advance = req.advance()==null ? BigDecimal.ZERO : req.advance();
        BigDecimal amount = req.amount()==null ? BigDecimal.ZERO : req.amount();
        validate(req, amount, advance);
        TailorOrder o = TailorOrder.builder()
                .customer(c)
                .dressType(req.dressType())
                .quantity(req.quantity())
                .amount(amount)
                .advance(advance)
                .balance(amount.subtract(advance))
                .paymentMode(req.paymentMode()==null ? PaymentMode.CASH : req.paymentMode())
                .status(OrderStatus.PENDING)
                .deliveryDate(req.deliveryDate())
                .measurementId(m==null ? null : m.getId())
                .measurementSnapshot(snapshot(m))
                .specialNotes(req.specialNotes())
                .build();
        TailorOrder saved = orders.save(o);
        saved.setOrderCode("TP-" + (1000 + saved.getId()));
        return orders.save(saved);
    }

    public TailorOrder update(Long id, OrderRequest req){
        TailorOrder o = orders.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        Customer c = customers.findById(req.customerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        Measurement m = resolveMeasurement(req.measurementId(), c.getId(), req.dressType());
        BigDecimal advance = req.advance()==null ? BigDecimal.ZERO : req.advance();
        BigDecimal amount = req.amount()==null ? BigDecimal.ZERO : req.amount();
        validate(req, amount, advance);
        o.setCustomer(c);
        o.setDressType(req.dressType());
        o.setQuantity(req.quantity());
        o.setAmount(amount);
        o.setAdvance(advance);
        o.setBalance(amount.subtract(advance));
        o.setPaymentMode(req.paymentMode()==null ? PaymentMode.CASH : req.paymentMode());
        o.setDeliveryDate(req.deliveryDate());
        o.setMeasurementId(m==null ? null : m.getId());
        o.setMeasurementSnapshot(snapshot(m));
        o.setSpecialNotes(req.specialNotes());
        return orders.save(o);
    }

    public TailorOrder status(Long id, OrderStatus status){
        TailorOrder o = orders.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if(status==OrderStatus.CANCELLED) throw new RuntimeException("Cancelled status is not used in this simple shop flow");
        o.setStatus(status);
        return orders.save(o);
    }

    private void validate(OrderRequest req, BigDecimal amount, BigDecimal advance){
        if(req.dressType()==null) throw new RuntimeException("Dress type is required");
        if(req.quantity()==null || req.quantity()<1) throw new RuntimeException("Quantity must be at least 1");
        if(amount.compareTo(BigDecimal.ZERO)<=0) throw new RuntimeException("Amount must be greater than 0");
        if(advance.compareTo(BigDecimal.ZERO)<0) throw new RuntimeException("Advance cannot be negative");
        if(advance.compareTo(amount)>0) throw new RuntimeException("Advance cannot be greater than total amount");
        if(req.deliveryDate()==null) throw new RuntimeException("Delivery date is required");
        if(req.deliveryDate().isBefore(LocalDate.now().minusDays(30))) throw new RuntimeException("Delivery date is too old. Please check the date.");
    }

    private Measurement resolveMeasurement(Long measurementId, Long customerId, DressType orderDressType){
        if(measurementId==null) return null;
        Measurement m = measurements.findById(measurementId).orElseThrow(() -> new RuntimeException("Measurement not found"));
        if(m.getCustomer()==null || !Objects.equals(m.getCustomer().getId(), customerId)) throw new RuntimeException("Selected measurement does not belong to this customer");
        if(orderDressType!=null && m.getDressType()!=null && m.getDressType()!=orderDressType) throw new RuntimeException("Selected measurement type does not match order dress type");
        return m;
    }

    private String snapshot(Measurement m){
        if(m==null) return "No measurement selected";
        List<String> parts = new ArrayList<>();
        if(m.getDressType()==DressType.PANT){
            add(parts,"Waist",m.getWaist()); add(parts,"Hip/Seat",m.getHip()); add(parts,"Thigh",m.getThigh()); add(parts,"Inseam",m.getInseam()); add(parts,"Full length",m.getLength()); add(parts,"Bottom",m.getBottom());
        } else {
            add(parts,"Chest",m.getChest()); add(parts,"Shoulder",m.getShoulder()); add(parts,"Sleeve",m.getSleeve()); add(parts,"Length",m.getLength()); add(parts,"Neck",m.getNeck()); add(parts,"Waist",m.getWaist());
        }
        if(m.getNotes()!=null && !m.getNotes().isBlank()) parts.add("Notes: "+m.getNotes());
        return m.getDressType()+" measurement - "+String.join(", ", parts);
    }
    private void add(List<String> p, String k, Double v){ if(v!=null) p.add(k+" "+v); }
}
