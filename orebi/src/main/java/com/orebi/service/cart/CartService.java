package com.orebi.service.cart;

import java.util.List;
import java.util.Optional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.OrderDTO;
import com.orebi.entity.Cart;

public interface CartService {
    Cart getOrCreateCartEntity(Long userId);

    Optional<CartDTO> getCartByUserId();

    OrderDTO checkout(List<Long> selectedLineItemIds, OrderDTO orderDTO);

}
