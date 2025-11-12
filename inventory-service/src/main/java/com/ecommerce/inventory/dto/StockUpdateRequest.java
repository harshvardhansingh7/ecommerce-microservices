package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateRequest {
    @NotNull
    private Integer quantity;
    
    private Operation operation;

    public enum Operation {
        INCREMENT, DECREMENT, SET
    }
}