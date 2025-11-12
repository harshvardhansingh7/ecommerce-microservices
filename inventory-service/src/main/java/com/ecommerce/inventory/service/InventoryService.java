package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryDTO;
import com.ecommerce.inventory.dto.OrderEvent;
import com.ecommerce.inventory.dto.StockUpdateRequest;
import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
        return convertToDTO(inventory);
    }

    public InventoryDTO updateStock(Long productId, StockUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        switch (request.getOperation()) {
            case INCREMENT -> inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
            case DECREMENT -> {
                if (inventory.getAvailableQuantity() < request.getQuantity()) {
                    throw new RuntimeException("Insufficient stock");
                }
                inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
            }
            case SET -> inventory.setQuantity(request.getQuantity());
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDTO(updatedInventory);
    }

    public InventoryDTO createInventory(Long productId, Integer initialQuantity) {
        if (inventoryRepository.existsByProductId(productId)) {
            throw new RuntimeException("Inventory already exists for product: " + productId);
        }

        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(initialQuantity);
        inventory.setReservedQuantity(0);

        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDTO(savedInventory);
    }

    public void reserveStockForOrder(OrderEvent orderEvent) {
        for (OrderEvent.OrderItem item : orderEvent.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + item.getProductId()));

            inventory.reserveQuantity(item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public void commitStockForOrder(OrderEvent orderEvent) {
        for (OrderEvent.OrderItem item : orderEvent.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + item.getProductId()));

            inventory.commitReservation(item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public void releaseStockForOrder(OrderEvent orderEvent) {
        for (OrderEvent.OrderItem item : orderEvent.getItems()) {
            inventoryRepository.findByProductId(item.getProductId())
                    .ifPresent(inventory -> {
                        inventory.releaseQuantity(item.getQuantity());
                        inventoryRepository.save(inventory);
                    });
        }
    }

    public boolean checkStockAvailability(List<OrderEvent.OrderItem> items) {
        for (OrderEvent.OrderItem item : items) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + item.getProductId()));

            if (!inventory.isAvailable(item.getQuantity())) {
                return false;
            }
        }
        return true;
    }

    public List<InventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setQuantity(inventory.getQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setCreatedAt(inventory.getCreatedAt());
        dto.setUpdatedAt(inventory.getUpdatedAt());
        return dto;
    }
}