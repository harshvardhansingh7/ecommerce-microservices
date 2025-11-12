package com.ecommerce.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private List<OrderItem> items;
    private OrderStatus status;

    public enum OrderStatus {
        CREATED, CONFIRMED, CANCELLED
    }

    @Data
    public static class OrderItem {
        private Long productId;
        private Integer quantity;
    }
}