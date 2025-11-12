package com.ecommerce.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}