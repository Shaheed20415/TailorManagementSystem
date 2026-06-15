package com.tailorpro.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class ShopSettings {
    @Id
    private Long id;
    private String shopName;
    private String ownerName;
    private String phone;
    private String address;
    private String invoicePrefix;
    private BigDecimal defaultDiscount;
    @Column(length = 1000)
    private String invoiceTerms;
}
