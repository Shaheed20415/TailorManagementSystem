package com.tailorpro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Measurement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DressType dressType;

    private Double chest;
    private Double waist;
    private Double shoulder;
    private Double sleeve;
    private Double length;
    private Double neck;
    private Double hip;
    private Double thigh;
    private Double inseam;
    private Double bottom;
    @Column(length = 1000)
    private String notes;
    private LocalDateTime createdAt;

    @JsonIgnoreProperties({"orders", "measurements", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @PrePersist public void prePersist(){ createdAt = LocalDateTime.now(); }
}
