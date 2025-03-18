package com.darknorth.peerisland.orderservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darknorth.peerisland.orderservice.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    // Find active products
    List<Product> findByActiveTrue();
    
    // Find products with stock less than a certain quantity
    List<Product> findByStockQuantityLessThan(int stockQuantity);
    
}
