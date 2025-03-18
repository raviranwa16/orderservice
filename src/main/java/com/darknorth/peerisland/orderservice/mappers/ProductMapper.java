package com.darknorth.peerisland.orderservice.mappers;

import org.springframework.stereotype.Component;

import com.darknorth.peerisland.orderservice.dtos.ProductDto;
import com.darknorth.peerisland.orderservice.models.Product;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    /**
     * Convert Product entity to ProductDto.Response
     */
    public ProductDto.Response toDto(Product product) {
        return ProductDto.Response.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert ProductDto.Request to Product entity
     */
    public Product toEntity(ProductDto.Request dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setActive(dto.isActive());
        return product;
    }
    
    /**
     * Convert list of Product entities to list of ProductDto.Response
     */
    public List<ProductDto.Response> toDtoList(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Apply updates from Request DTO to existing entity
     */
    public void updateEntityFromDto(ProductDto.Request dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setActive(dto.isActive());
        // Stock is managed separately
    }
}
