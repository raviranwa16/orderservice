package com.darknorth.peerisland.orderservice.services.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.darknorth.peerisland.orderservice.exceptions.CustomerNotFoundException;
import com.darknorth.peerisland.orderservice.models.Customer;
import com.darknorth.peerisland.orderservice.repositories.CustomerRepository;
import com.darknorth.peerisland.orderservice.services.CustomerService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        // Check if email already exists
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + customer.getEmail() + " already exists");
        }
        
        log.info("Creating new customer with email: {}", customer.getEmail());
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer updateCustomer(UUID id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        
        // Check if trying to update email to one that already exists
        if (!customer.getEmail().equals(customerDetails.getEmail()) && 
            customerRepository.existsByEmail(customerDetails.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + customerDetails.getEmail());
        }
        
        // Update customer fields
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        
        log.info("Updating customer: {}", id);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        // First check if customer exists
        Customer customer = getCustomerById(id);
        
        // Check if customer has orders before deleting
        if (!customer.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete customer with existing orders");
        }
        
        log.info("Deleting customer: {}", id);
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerWithOrders(UUID id) {
        Customer customer = getCustomerById(id);
        
        // Force initialization of orders (Hibernate lazy loading)
        customer.getOrders().size();
        
        return customer;
    }
}