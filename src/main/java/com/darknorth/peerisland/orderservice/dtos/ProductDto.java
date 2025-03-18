package com.darknorth.peerisland.orderservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Name is required")
        private String name;
        
        private String description;
        
        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        private Double price;
        
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
        private Integer stockQuantity;
        
        @Builder.Default
        private boolean active = true;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String name;
        private String description;
        private Double price;
        private Integer stockQuantity;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockUpdateRequest {
        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
