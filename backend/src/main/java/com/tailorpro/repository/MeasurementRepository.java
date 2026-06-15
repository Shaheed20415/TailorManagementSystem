package com.tailorpro.repository;
import com.tailorpro.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MeasurementRepository extends JpaRepository<Measurement, Long> { List<Measurement> findByCustomerIdOrderByCreatedAtDesc(Long customerId); }
