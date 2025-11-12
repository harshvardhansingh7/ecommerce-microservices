package com.ecommerce.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Integer getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public boolean isAvailable(Integer requestedQuantity) {
        return getAvailableQuantity() >= requestedQuantity;
    }

    public void reserveQuantity(Integer quantity) {
        if (!isAvailable(quantity)) {
            throw new RuntimeException("Insufficient stock available");
        }
        this.reservedQuantity += quantity;
    }

    public void releaseQuantity(Integer quantity) {
        if (this.reservedQuantity < quantity) {
            throw new RuntimeException("Cannot release more than reserved quantity");
        }
        this.reservedQuantity -= quantity;
    }

    public void commitReservation(Integer quantity) {
        if (this.reservedQuantity < quantity) {
            throw new RuntimeException("Cannot commit more than reserved quantity");
        }
        this.quantity -= quantity;
        this.reservedQuantity -= quantity;
    }
}