package com.darknorth.peerisland.orderservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darknorth.peerisland.orderservice.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // Find customer by email
    Optional<Customer> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);
}
