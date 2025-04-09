package com.orebi.service.cart;

import com.orebi.dto.CartDTO;
import java.util.Optional;

public interface CartService {
    Optional<CartDTO> getCartByUserId();
    CartDTO addItemToCart(Long productId, int quantity);
    CartDTO removeItemFromCart( Long productId);
    void clearCart();
}
