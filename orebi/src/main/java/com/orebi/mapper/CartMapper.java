package com.orebi.mapper;

import com.orebi.dto.CartDTO;
import com.orebi.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper implements EntityMapper<CartDTO, Cart> {
    private final LineItemMapper lineItemMapper;

    public CartMapper(LineItemMapper lineItemMapper) {
        this.lineItemMapper = lineItemMapper;
    }

    @Override
    public CartDTO toDTO(Cart cart) {
        if (cart == null) return null;
        
        return new CartDTO(
                cart.getCartId(),
                cart.getUser().getUserId(),
                lineItemMapper.toDTOList(cart.getLineItems())
        );
    }

    @Override
    public Cart toEntity(CartDTO cartDTO) {
        if (cartDTO == null) return null;
        
        Cart cart = new Cart();
        cart.setCartId(cartDTO.getCartId());
        return cart;
    }
}
