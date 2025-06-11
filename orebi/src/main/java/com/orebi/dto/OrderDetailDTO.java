package com.orebi.dto;

public class OrderDetailDTO {
    private Long orderDetailId;
    private Long orderId;

    private int quantity;
    private double unitPrice;
    private double totalPrice;

    private Long snapshotProductId;
    private String snapshotProductName;
    private String snapshotProductImage;
    private Double snapshotPrice;
    private Double shippingFee;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getSnapshotProductId() {
        return snapshotProductId;
    }

    public void setSnapshotProductId(Long snapshotProductId) {
        this.snapshotProductId = snapshotProductId;
    }

    public String getSnapshotProductName() {
        return snapshotProductName;
    }

    public void setSnapshotProductName(String snapshotProductName) {
        this.snapshotProductName = snapshotProductName;
    }

    public String getSnapshotProductImage() {
        return snapshotProductImage;
    }

    public void setSnapshotProductImage(String snapshotProductImage) {
        this.snapshotProductImage = snapshotProductImage;
    }

    public Double getSnapshotPrice() {
        return snapshotPrice;
    }

    public void setSnapshotPrice(Double snapshotPrice) {
        this.snapshotPrice = snapshotPrice;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }
}
