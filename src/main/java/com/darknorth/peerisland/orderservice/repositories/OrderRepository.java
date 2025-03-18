package com.darknorth.peerisland.orderservice.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.darknorth.peerisland.orderservice.enums.OrderStatus;
import com.darknorth.peerisland.orderservice.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    // Find all orders by status
    List<Order> findByStatus(OrderStatus status);
    
    // Find all orders by customer id
    List<Order> findByCustomerId(UUID customerId);
    
    // Find orders by status and created before certain time (used for automatic processing)
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt <= :beforeTime")
    List<Order> findOrdersToProcess(OrderStatus status, LocalDateTime beforeTime);
    
    // Count orders by status
    long countByStatus(OrderStatus status);
}