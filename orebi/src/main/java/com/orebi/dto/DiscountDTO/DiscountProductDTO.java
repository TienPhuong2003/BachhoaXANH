package com.orebi.dto.DiscountDTO;

public class DiscountProductDTO extends DiscountBaseDTO {
    private double minProductPrice;
    private int quantity;

    public double getMinProductPrice() {
        return minProductPrice;
    }

    public void setMinProductPrice(double minProductPrice) {
        this.minProductPrice = minProductPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
