package com.orebi.service.cart;

import java.util.List;
import java.util.Optional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.request.UpdateCartRequest;

public interface CartService {
    Optional<CartDTO> getCartByUserId();

    CartDTO updateCartItems(List<UpdateCartRequest.CartItemUpdate> items);
}
