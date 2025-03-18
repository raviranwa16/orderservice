package com.darknorth.peerisland.orderservice.services.impls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darknorth.peerisland.orderservice.enums.OrderStatus;
import com.darknorth.peerisland.orderservice.exceptions.OrderNotFoundException;
import com.darknorth.peerisland.orderservice.exceptions.OrderStatusException;
import com.darknorth.peerisland.orderservice.models.Order;
import com.darknorth.peerisland.orderservice.models.OrderItem;
import com.darknorth.peerisland.orderservice.models.Product;
import com.darknorth.peerisland.orderservice.repositories.OrderRepository;
import com.darknorth.peerisland.orderservice.repositories.ProductRepository;
import com.darknorth.peerisland.orderservice.services.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor 
@Slf4j
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    ProductRepository productRepository;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // Set initial status
        order.setStatus(OrderStatus.PENDING);
        
        // Update product inventory and validate stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            
            // Refresh product from database to get latest stock
            Product freshProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + product.getId()));
            
            // Check if enough stock is available
            if (!freshProduct.hasStock(item.getQuantity())) {
                throw new IllegalArgumentException(
                        "Not enough stock for product: " + freshProduct.getName() + 
                        ". Available: " + freshProduct.getStockQuantity() + 
                        ", Requested: " + item.getQuantity());
            }
            
            // Reduce stock
            freshProduct.reduceStock(item.getQuantity());
            productRepository.save(freshProduct);
        }
        
        log.info("Creating new order for customer: {}", order.getCustomer().getId());
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    @Override
    @Transactional
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order order = getOrderById(id);
        
        // Validate status transition
        validateStatusTransition(order.getStatus(), status);
        
        log.info("Updating order {} status from {} to {}", id, order.getStatus(), status);
        order.updateStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders(OrderStatus status) {
        if (status != null) {
            return orderRepository.findByStatus(status);
        }
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order cancelOrder(UUID id) {
        Order order = getOrderById(id);
        
        if (!order.isCancellable()) {
            throw new OrderStatusException("Order cannot be cancelled. Current status: " + order.getStatus());
        }
        
        // Restore product inventory
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProduct().getId()));
            
            product.restoreStock(item.getQuantity());
            productRepository.save(product);
        }
        
        log.info("Cancelling order: {}", id);
        order.updateStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public int processPendingOrders() {
        // Find orders that have been in PENDING status for at least 5 minutes
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<Order> pendingOrders = orderRepository.findOrdersToProcess(OrderStatus.PENDING, fiveMinutesAgo);
        
        log.info("Processing {} pending orders", pendingOrders.size());
        
        int processedCount = 0;
        for (Order order : pendingOrders) {
            try {
                order.updateStatus(OrderStatus.PROCESSING);
                orderRepository.save(order);
                processedCount++;
            } catch (Exception e) {
                log.error("Error processing order {}: {}", order.getId(), e.getMessage(), e);
            }
        }
        
        log.info("Successfully processed {} orders", processedCount);
        return processedCount;
    }
    
    // Helper method to validate status transitions
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Implement rules for valid status transitions
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new OrderStatusException("Cannot change status of a cancelled order");
        }
        
        if (currentStatus == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
            throw new OrderStatusException("Cannot change status of a delivered order");
        }
        
        if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.SHIPPED) {
            throw new OrderStatusException("Order must be in PROCESSING status before it can be shipped");
        }
        
        if (currentStatus == OrderStatus.PROCESSING && newStatus == OrderStatus.DELIVERED) {
            throw new OrderStatusException("Order must be SHIPPED before it can be DELIVERED");
        }
    }
}
