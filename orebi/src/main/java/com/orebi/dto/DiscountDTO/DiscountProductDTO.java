package com.orebi.dto.DiscountDTO;

public class DiscountProductDTO extends DiscountBaseDTO {
    private double discountValue;
    private boolean isPercentage;

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

}
