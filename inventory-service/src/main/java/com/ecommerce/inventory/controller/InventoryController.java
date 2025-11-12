package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryDTO;
import com.ecommerce.inventory.dto.StockUpdateRequest;
import com.ecommerce.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Management", description = "APIs for stock management")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get inventory by product ID")
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable Long productId) {
        InventoryDTO inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{productId}/update")
    @Operation(summary = "Update stock quantity")
    public ResponseEntity<InventoryDTO> updateStock(@PathVariable Long productId,
                                                    @Valid @RequestBody StockUpdateRequest request) {
        InventoryDTO inventory = inventoryService.updateStock(productId, request);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "Create inventory for product")
    public ResponseEntity<InventoryDTO> createInventory(@PathVariable Long productId,
                                                        @RequestParam Integer initialQuantity) {
        InventoryDTO inventory = inventoryService.createInventory(productId, initialQuantity);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping
    @Operation(summary = "Get all inventory")
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        List<InventoryDTO> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }
}