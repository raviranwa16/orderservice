package com.darknorth.peerisland.orderservice.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name= "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public boolean hasStock(int quantity){
        return this.stockQuantity >= quantity;
    }

    public void reduceStock(int quantity){
        if (!hasStock(quantity)){
            throw new IllegalArgumentException("Not enough stock available for product: " + name);
        }
        this.stockQuantity -= quantity;
    }

    public void restoreStock(int quantity){
        this.stockQuantity += quantity;
    }
}
