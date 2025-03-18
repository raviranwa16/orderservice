package com.darknorth.peerisland.orderservice.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.darknorth.peerisland.orderservice.services.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingJob {
     private final OrderService orderService;
    
    /**
     * Scheduled job to automatically process pending orders
     * Runs every 5 minutes as specified in application.properties
     */
    @Scheduled(cron = "${order.processing.cron}")
    public void processPendingOrders() {
        log.info("Starting scheduled job to process pending orders");
        try {
            int processedCount = orderService.processPendingOrders();
            log.info("Scheduled job completed. Processed {} orders", processedCount);
        } catch (Exception e) {
            log.error("Error in order processing job: {}", e.getMessage(), e);
        }
    }
}
