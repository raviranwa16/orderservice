package com.darknorth.peerisland.orderservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.darknorth.peerisland.orderservice.dtos.ProductDto;
import com.darknorth.peerisland.orderservice.mappers.ProductMapper;
import com.darknorth.peerisland.orderservice.models.Product;
import com.darknorth.peerisland.orderservice.services.ProductService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;
    
    /**
     * Create a new product
     */
    @PostMapping
    public ResponseEntity<ProductDto.Response> createProduct(@Valid @RequestBody ProductDto.Request request) {
        log.info("Creating new product: {}", request.getName());
        
        Product product = productMapper.toEntity(request);
        Product createdProduct = productService.createProduct(product);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.toDto(createdProduct));
    }
    
    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.Response> getProduct(@PathVariable UUID id) {
        log.info("Fetching product: {}", id);
        
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }
    
    /**
     * Get all products
     */
    @GetMapping
    public ResponseEntity<List<ProductDto.Response>> getAllProducts(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        
        log.info("Fetching all products, activeOnly: {}", activeOnly);
        
        List<Product> products;
        if (activeOnly) {
            products = productService.getAllActiveProducts();
        } else {
            products = productService.getAllProducts();
        }
        
        return ResponseEntity.ok(productMapper.toDtoList(products));
    }
    
    /**
     * Update a product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto.Response> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDto.Request request) {
        
        log.info("Updating product: {}", id);
        
        Product productDetails = productMapper.toEntity(request);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }
    
    /**
     * Delete a product (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto.Response> deleteProduct(@PathVariable UUID id) {
        log.info("Deactivating product: {}", id);
        
        Product deactivatedProduct = productService.deleteProduct(id);
        return ResponseEntity.ok(productMapper.toDto(deactivatedProduct));
    }
    
    /**
     * Update product stock
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDto.Response> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDto.StockUpdateRequest request) {
        
        log.info("Updating stock for product {}: {}", id, request.getQuantity());
        
        Product updatedProduct = productService.updateStock(id, request.getQuantity());
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }
    
    /**
     * Get products with low stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto.Response>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        
        log.info("Fetching products with stock below: {}", threshold);
        
        List<Product> lowStockProducts = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(productMapper.toDtoList(lowStockProducts));
    }
}
