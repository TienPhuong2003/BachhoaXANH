package com.orebi.service.cart;

import java.util.Optional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.request.CheckoutRequest;
import com.orebi.entity.Cart;

public interface CartService {
    Cart getOrCreateCartEntity(Long userId);

    Optional<CartDTO> getCartByUserId();

    void checkout(CheckoutRequest checkoutRequest);
}
