package com.ecommerce.order.dto;

import com.ecommerce.order.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @NotNull
    private Long userId;

    @Valid
    private Order.ShippingAddress shippingAddress;

    @NotNull
    private List<OrderItemDTO> items;
}