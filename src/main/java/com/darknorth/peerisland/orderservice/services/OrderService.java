package com.darknorth.peerisland.orderservice.services;

import java.util.List;
import java.util.UUID;

import com.darknorth.peerisland.orderservice.enums.OrderStatus;
import com.darknorth.peerisland.orderservice.models.Order;

public interface OrderService {
    
    /**
     * Create a new order
     * @param order The order to create
     * @return The created order
     */
    Order createOrder(Order order);
    
    /**
     * Get an order by ID
     * @param id The order ID
     * @return The order if found
     */
    Order getOrderById(UUID id);
    
    /**
     * Update an order's status
     * @param id The order ID
     * @param status The new status
     * @return The updated order
     */
    Order updateOrderStatus(UUID id, OrderStatus status);
    
    /**
     * Get all orders, optionally filtered by status
     * @param status The status to filter by (optional)
     * @return List of orders
     */
    List<Order> getAllOrders(OrderStatus status);
    
    /**
     * Cancel an order if it's still in PENDING status
     * @param id The order ID
     * @return The cancelled order
     */
    Order cancelOrder(UUID id);
    
    /**
     * Process PENDING orders automatically (for scheduled job)
     * @return Number of orders processed
     */
    int processPendingOrders();
}