package com.darknorth.peerisland.orderservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.darknorth.peerisland.orderservice.dtos.CustomerDto;
import com.darknorth.peerisland.orderservice.mappers.CustomerMapper;
import com.darknorth.peerisland.orderservice.models.Customer;
import com.darknorth.peerisland.orderservice.services.CustomerService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;
    
    /**
     * Create a new customer
     */
    @PostMapping
    public ResponseEntity<CustomerDto.Response> createCustomer(@Valid @RequestBody CustomerDto.Request request) {
        log.info("Creating new customer with email: {}", request.getEmail());
        
        Customer customer = customerMapper.toEntity(request);
        Customer createdCustomer = customerService.createCustomer(customer);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerMapper.toDto(createdCustomer));
    }
    
    /**
     * Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto.Response> getCustomer(@PathVariable UUID id) {
        log.info("Fetching customer: {}", id);
        
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerMapper.toDto(customer));
    }
    
    /**
     * Get customer by ID with orders
     */
    @GetMapping("/{id}/with-orders")
    public ResponseEntity<CustomerDto.DetailedResponse> getCustomerWithOrders(@PathVariable UUID id) {
        log.info("Fetching customer with orders: {}", id);
        
        Customer customer = customerService.getCustomerWithOrders(id);
        return ResponseEntity.ok(customerMapper.toDetailedDto(customer));
    }
    
    /**
     * Get all customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerDto.Response>> getAllCustomers() {
        log.info("Fetching all customers");
        
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customerMapper.toDtoList(customers));
    }
    
    /**
     * Update a customer
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto.Response> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerDto.Request request) {
        
        log.info("Updating customer: {}", id);
        
        Customer customerDetails = customerMapper.toEntity(request);
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        
        return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
    }
    
    /**
     * Delete a customer
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        log.info("Deleting customer: {}", id);
        
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
