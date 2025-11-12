package com.ecommerce.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddToCartRequest {
    @NotNull
    private Long productId;
    
    @NotNull
    @Positive
    private Integer quantity;
    
    private String productName;
    private BigDecimal price;
    private String imageUrl;
}