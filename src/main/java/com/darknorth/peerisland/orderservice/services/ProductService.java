package com.darknorth.peerisland.orderservice.services;

import java.util.List;
import java.util.UUID;

import com.darknorth.peerisland.orderservice.models.Product;

public interface ProductService {
    
    /**
     * Create a new product
     * @param product The product to create
     * @return The created product
     */
    Product createProduct(Product product);
    
    /**
     * Get a product by ID
     * @param id The product ID
     * @return The product if found
     */
    Product getProductById(UUID id);
    
    /**
     * Get all products
     * @return List of all products
     */
    List<Product> getAllProducts();
    
    /**
     * Get all active products
     * @return List of active products
     */
    List<Product> getAllActiveProducts();
    
    /**
     * Update a product
     * @param id The product ID
     * @param product The updated product details
     * @return The updated product
     */
    Product updateProduct(UUID id, Product product);
    
    /**
     * Delete a product (deactivates it)
     * @param id The product ID
     * @return The deactivated product
     */
    Product deleteProduct(UUID id);
    
    /**
     * Update product stock
     * @param id The product ID
     * @param quantity The quantity to add (positive) or subtract (negative)
     * @return The updated product
     */
    Product updateStock(UUID id, int quantity);
    
    /**
     * Get products with low stock (below threshold)
     * @param threshold The low stock threshold
     * @return List of products with stock below threshold
     */
    List<Product> getLowStockProducts(int threshold);
}
