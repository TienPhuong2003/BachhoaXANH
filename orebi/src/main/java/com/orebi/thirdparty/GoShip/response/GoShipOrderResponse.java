package com.orebi.thirdparty.GoShip.response;

import java.time.LocalDateTime;

public class GoShipOrderResponse {
    private String status;
    private String id;
    private Double cod;
    private Double shipFee;
    private String tracking_number;
    private String carrier;
    private String carrier_short_name;
    private LocalDateTime createdAt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCod() {
        return cod;
    }

    public void setCod(Double cod) {
        this.cod = cod;
    }

    public Double getShipFee() {
        return shipFee;
    }

    public void setShipFee(Double shipFee) {
        this.shipFee = shipFee;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrier_short_name() {
        return carrier_short_name;
    }

    public void setCarrier_short_name(String carrier_short_name) {
        this.carrier_short_name = carrier_short_name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
