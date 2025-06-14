package com.orebi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.CartDTO;
import com.orebi.dto.OrderDTO;
import com.orebi.dto.request.CheckoutRequest;
import com.orebi.service.cart.CartService;

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

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
        OrderDTO checkout = cartService.checkout(request.getSelectedLineItemIds(), request.getOrder());
        return ResponseEntity.ok(checkout);
    }
}
