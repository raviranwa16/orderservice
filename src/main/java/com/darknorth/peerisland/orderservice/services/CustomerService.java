package com.darknorth.peerisland.orderservice.services;

import java.util.List;
import java.util.UUID;

import com.darknorth.peerisland.orderservice.models.Customer;

public interface CustomerService {
    
    /**
     * Create a new customer
     * @param customer The customer to create
     * @return The created customer
     */
    Customer createCustomer(Customer customer);
    
    /**
     * Get a customer by ID
     * @param id The customer ID
     * @return The customer if found
     */
    Customer getCustomerById(UUID id);
    
    /**
     * Get a customer by email
     * @param email The customer email
     * @return The customer if found
     */
    Customer getCustomerByEmail(String email);
    
    /**
     * Get all customers
     * @return List of all customers
     */
    List<Customer> getAllCustomers();
    
    /**
     * Update a customer
     * @param id The customer ID
     * @param customer The updated customer details
     * @return The updated customer
     */
    Customer updateCustomer(UUID id, Customer customer);
    
    /**
     * Delete a customer by ID
     * @param id The customer ID
     */
    void deleteCustomer(UUID id);
    
    /**
     * Get all orders for a customer
     * @param id The customer ID
     * @return The customer with loaded orders
     */
    Customer getCustomerWithOrders(UUID id);
}