package com.ecommerce.notification.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private String userEmail;
    private String userName;
}