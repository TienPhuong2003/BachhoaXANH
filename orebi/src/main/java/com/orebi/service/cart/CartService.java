package com.orebi.service.cart;

import java.util.Optional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.request.CheckoutRequest;

public interface CartService {
    Optional<CartDTO> getCartByUserId();

    void checkout(CheckoutRequest checkoutRequest);
}
