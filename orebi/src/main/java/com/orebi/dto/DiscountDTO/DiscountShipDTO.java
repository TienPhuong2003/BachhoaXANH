package com.orebi.dto.DiscountDTO;

public class DiscountShipDTO extends DiscountBaseDTO {
    private double maxShipDiscount;
    private double minShipDiscount;

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

}
