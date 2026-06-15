package com.tailorpro.repository;
import com.tailorpro.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMobile(String mobile);
    List<Customer> findByNameContainingIgnoreCaseOrMobileContaining(String name, String mobile);
}
