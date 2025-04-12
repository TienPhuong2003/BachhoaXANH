package com.orebi.dto.request;

import java.util.List;

public class UpdateCartRequest {
    private List<CartItemUpdate> items;

    public static class CartItemUpdate {
        private Long productId;
        private int quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

    }

    public List<CartItemUpdate> getItems() {
        return items;
    }

    public void setItems(List<CartItemUpdate> items) {
        this.items = items;
    }

}
