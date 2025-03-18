package com.darknorth.peerisland.orderservice.services.impls;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.darknorth.peerisland.orderservice.exceptions.ProductNotFoundException;
import com.darknorth.peerisland.orderservice.exceptions.ProductStockException;
import com.darknorth.peerisland.orderservice.models.Product;
import com.darknorth.peerisland.orderservice.repositories.ProductRepository;
import com.darknorth.peerisland.orderservice.services.ProductService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, Product productDetails) {
        Product product = getProductById(id);
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setActive(productDetails.isActive());
        
        // Don't update stock through this method
        
        log.info("Updating product: {}", id);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product deleteProduct(UUID id) {
        Product product = getProductById(id);
        
        // Soft delete - mark as inactive
        product.setActive(false);
        
        log.info("Deactivating product: {}", id);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateStock(UUID id, int quantity) {
        Product product = getProductById(id);
        
        // Handle stock reduction
        if (quantity < 0 && !product.hasStock(Math.abs(quantity))) {
            throw new ProductStockException("Not enough stock for product: " + product.getName());
        }
        
        // Update stock
        product.setStockQuantity(product.getStockQuantity() + quantity);
        
        log.info("Updating stock for product {} by {}", id, quantity);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }
}
