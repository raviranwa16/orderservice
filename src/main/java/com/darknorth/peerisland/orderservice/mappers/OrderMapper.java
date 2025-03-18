package com.darknorth.peerisland.orderservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.darknorth.peerisland.orderservice.dtos.OrderDto;
import com.darknorth.peerisland.orderservice.dtos.OrderItemDto;
import com.darknorth.peerisland.orderservice.models.Customer;
import com.darknorth.peerisland.orderservice.models.Order;
import com.darknorth.peerisland.orderservice.models.OrderItem;
import com.darknorth.peerisland.orderservice.models.Product;
import com.darknorth.peerisland.orderservice.repositories.CustomerRepository;
import com.darknorth.peerisland.orderservice.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    
    /**
     * Convert Order entity to OrderDto.Response
     */
    public OrderDto.Response toDto(Order order) {
        return OrderDto.Response.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getFullName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancellable(order.isCancellable())
                .build();
    }
    
    /**
     * Convert OrderItem entity to OrderItemDto.Response
     */
    public OrderItemDto.Response toDto(OrderItem orderItem) {
        return OrderItemDto.Response.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subtotal(orderItem.getSubtotal())
                .build();
    }
    
    /**
     * Convert OrderDto.Request to Order entity
     */
    public Order toEntity(OrderDto.Request dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + dto.getCustomerId()));
        
        Order order = new Order();
        order.setCustomer(customer);
        
        // Add items
        List<OrderItem> items = dto.getItems().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        
        items.forEach(order::addItem);
        
        return order;
    }
    
    /**
     * Convert OrderItemDto.Request to OrderItem entity
     */
    public OrderItem toEntity(OrderItemDto.Request dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));
        
        return new OrderItem(product, dto.getQuantity());
    }
    
    /**
     * Convert list of Order entities to list of OrderDto.Response
     */
    public List<OrderDto.Response> toDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}