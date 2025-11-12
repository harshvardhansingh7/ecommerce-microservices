package com.ecommerce.order.dto;

import com.ecommerce.order.model.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private String orderNumber;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private Order.ShippingAddress shippingAddress;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}