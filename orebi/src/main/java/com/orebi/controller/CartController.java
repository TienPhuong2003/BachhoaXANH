package com.orebi.controller;

import com.orebi.dto.CartDTO;
import com.orebi.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        return cartService.getCartByUserId()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartDTO updatedCart = cartService.addItemToCart(productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long productId) {
        CartDTO updatedCart = cartService.removeItemFromCart(productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
