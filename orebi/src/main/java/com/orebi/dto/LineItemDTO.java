package com.orebi.dto;

public class LineItemDTO {
    private Long lineItemId;
    private Long productId;
    private int quantity;
    private double totalPrice;

    public LineItemDTO() {
    }

    public LineItemDTO(Long lineItemId, Long productId, int quantity, double totalPrice) {
        this.lineItemId = lineItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}