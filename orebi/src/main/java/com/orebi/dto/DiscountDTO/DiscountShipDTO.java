package com.orebi.dto.DiscountDTO;

public class DiscountShipDTO extends DiscountBaseDTO {
    private double maxShipDiscount;
    private double minShipDiscount;
    private int quantity;

    public double getMaxShipDiscount() {
        return maxShipDiscount;
    }

    public void setMaxShipDiscount(double maxShipDiscount) {
        this.maxShipDiscount = maxShipDiscount;
    }

    public double getMinShipDiscount() {
        return minShipDiscount;
    }

    public void setMinShipDiscount(double minShipDiscount) {
        this.minShipDiscount = minShipDiscount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
