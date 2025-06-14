package com.orebi.thirdparty.GoShip.request;

public class GoShipFeeRequest {
    private Shipment shipment;

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public static class Shipment {
        private Address address_from;
        private Address address_to;
        private Parcel parcel;

        public Address getAddress_from() {
            return address_from;
        }

        public void setAddress_from(Address address_from) {
            this.address_from = address_from;
        }

        public Address getAddress_to() {
            return address_to;
        }

        public void setAddress_to(Address address_to) {
            this.address_to = address_to;
        }

        public Parcel getParcel() {
            return parcel;
        }

        public void setParcel(Parcel parcel) {
            this.parcel = parcel;
        }
    }

    public static class Address {
        private String district;
        private String city;

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    public static class Parcel {
        private double cod;
        private double amount;
        private int width = 10;
        private int height = 10;
        private int length = 10;
        private int weight = 750;

        public double getCod() {
            return cod;
        }

        public void setCod(double cod) {
            this.cod = cod;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getLength() {
            return length;
        }

        public int getWeight() {
            return weight;
        }
    }

}