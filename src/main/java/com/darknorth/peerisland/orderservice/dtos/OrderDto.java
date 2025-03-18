package com.darknorth.peerisland.orderservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.darknorth.peerisland.orderservice.enums.OrderStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Customer ID is required")
        private UUID customerId;
        
        @NotEmpty(message = "Order must contain at least one item")
        @Valid
        private List<OrderItemDto.Request> items;
        
        private String notes;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private UUID customerId;
        private String customerName;
        private OrderStatus status;
        private Double totalAmount;
        private List<OrderItemDto.Response> items;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime shippedAt;
        private LocalDateTime deliveredAt;
        private String notes;
        private boolean cancellable;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        @NotNull(message = "Status is required")
        private OrderStatus status;
    }
}
