package com.darknorth.peerisland.orderservice.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.darknorth.peerisland.orderservice.dtos.OrderDto;
import com.darknorth.peerisland.orderservice.enums.OrderStatus;
import com.darknorth.peerisland.orderservice.mappers.OrderMapper;
import com.darknorth.peerisland.orderservice.models.Order;
import com.darknorth.peerisland.orderservice.services.OrderService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;
    
    /**
     * Create a new order
     */
    @PostMapping
    public ResponseEntity<OrderDto.Response> createOrder(@Valid @RequestBody OrderDto.Request request) {
        log.info("Creating new order for customer: {}", request.getCustomerId());
        
        Order order = orderMapper.toEntity(request);
        Order createdOrder = orderService.createOrder(order);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.toDto(createdOrder));
    }
    
    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> getOrder(@PathVariable UUID id) {
        log.info("Fetching order: {}", id);
        
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }
    
    /**
     * Get all orders with optional status filter
     */
    @GetMapping
    public ResponseEntity<List<OrderDto.Response>> getAllOrders(
            @RequestParam(required = false) OrderStatus status) {
        
        log.info("Fetching all orders with status filter: {}", status);
        
        List<Order> orders = orderService.getAllOrders(status);
        return ResponseEntity.ok(orderMapper.toDtoList(orders));
    }
    
    /**
     * Update order status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto.Response> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderDto.StatusUpdateRequest request) {
        
        log.info("Updating order status: {} to {}", id, request.getStatus());
        
        Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
    }
    
    /**
     * Cancel an order
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDto.Response> cancelOrder(@PathVariable UUID id) {
        log.info("Cancelling order: {}", id);
        
        Order cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderMapper.toDto(cancelledOrder));
    }
}
