package com.orebi.dto;

import java.util.List;

public class CartDTO {
    private Long cartId;
    private Long userId;
    private List<LineItemDTO> lineItems;

    public CartDTO() {};

    public CartDTO(Long cartId, Long userId, List<LineItemDTO> lineItems) {
        this.cartId = cartId;
        this.userId = userId;
        this.lineItems = lineItems;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<LineItemDTO> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemDTO> lineItems) {
        this.lineItems = lineItems;
    }
}
