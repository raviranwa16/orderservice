package com.darknorth.peerisland.orderservice.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.darknorth.peerisland.orderservice.dtos.CustomerDto;
import com.darknorth.peerisland.orderservice.models.Customer;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final OrderMapper orderMapper;
    
    /**
     * Convert Customer entity to CustomerDto.Response
     */
    public CustomerDto.Response toDto(Customer customer) {
        return CustomerDto.Response.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert Customer entity to CustomerDto.DetailedResponse with orders
     */
    public CustomerDto.DetailedResponse toDetailedDto(Customer customer) {
        return CustomerDto.DetailedResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .orders(customer.getOrders().stream()
                        .map(orderMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
    
    /**
     * Convert CustomerDto.Request to Customer entity
     */
    public Customer toEntity(CustomerDto.Request dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        return customer;
    }
    
    /**
     * Convert list of Customer entities to list of CustomerDto.Response
     */
    public List<CustomerDto.Response> toDtoList(List<Customer> customers) {
        return customers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
