package com.tailorpro.repository;
import com.tailorpro.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.*;
import java.util.*;

public interface OrderRepository extends JpaRepository<TailorOrder, Long> {
    List<TailorOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<TailorOrder> findByStatus(OrderStatus status);
    List<TailorOrder> findByDeliveryDate(LocalDate date);
    List<TailorOrder> findByOrderDateBetween(LocalDate start, LocalDate end);
    long countByStatus(OrderStatus status);

    @Query("""
        select o from TailorOrder o join fetch o.customer c
        where (:status is null or o.status = :status)
        and (lower(c.name) like lower(concat('%', :q, '%')) or c.mobile like concat('%', :q, '%') or lower(o.orderCode) like lower(concat('%', :q, '%')))
        order by case when o.status = com.tailorpro.model.OrderStatus.PENDING then 0 when o.status = com.tailorpro.model.OrderStatus.READY then 1 else 2 end,
                 o.deliveryDate asc, o.createdAt asc
    """)
    List<TailorOrder> searchOrders(@Param("q") String q, @Param("status") OrderStatus status);

    @Query("""
        select o from TailorOrder o join fetch o.customer c
        where o.status in (com.tailorpro.model.OrderStatus.PENDING, com.tailorpro.model.OrderStatus.READY)
        order by o.deliveryDate asc, o.createdAt asc
    """)
    List<TailorOrder> workQueue();
}
