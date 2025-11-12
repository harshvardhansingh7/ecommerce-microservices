package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart Management", description = "APIs for shopping cart operations")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user's cart")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/add")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<CartDTO> addToCart(@PathVariable Long userId,
                                             @Valid @RequestBody AddToCartRequest request) {
        CartDTO cart = cartService.addItemToCart(userId, request);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{userId}/update")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long userId,
                                                  @RequestParam Long productId,
                                                  @RequestParam Integer quantity) {
        CartDTO cart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}/remove")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<CartDTO> removeFromCart(@PathVariable Long userId,
                                                  @RequestParam Long productId) {
        CartDTO cart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}/clear")
    @Operation(summary = "Clear entire cart")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}