package com.ecommerce.inventory.messaging;

import com.ecommerce.inventory.dto.OrderEvent;
import com.ecommerce.inventory.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final InventoryService inventoryService;

    public OrderEventListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "order-created")
    public void handleOrderCreated(OrderEvent orderEvent) {
        try {
            // Reserve stock when order is created
            inventoryService.reserveStockForOrder(orderEvent);
        } catch (Exception e) {
            // Handle failure - might need to send to DLQ
            System.err.println("Failed to reserve stock for order: " + orderEvent.getOrderId());
        }
    }

    @KafkaListener(topics = "order-confirmed")
    public void handleOrderConfirmed(OrderEvent orderEvent) {
        try {
            // Commit reserved stock when order is confirmed
            inventoryService.commitStockForOrder(orderEvent);
        } catch (Exception e) {
            System.err.println("Failed to commit stock for order: " + orderEvent.getOrderId());
        }
    }

    @KafkaListener(topics = "order-cancelled")
    public void handleOrderCancelled(OrderEvent orderEvent) {
        try {
            // Release reserved stock when order is cancelled
            inventoryService.releaseStockForOrder(orderEvent);
        } catch (Exception e) {
            System.err.println("Failed to release stock for order: " + orderEvent.getOrderId());
        }
    }
}