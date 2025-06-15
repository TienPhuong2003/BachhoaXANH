package com.orebi.thirdparty.GoShip.request;

public class GoShipOrderRequest {
    private Shipment shipment;

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public static class Shipment {
        private String rate;
        private int payer = 0;
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

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public int getPayer() {
            return payer;
        }

        public void setPayer(int payer) {
            this.payer = payer;
        }

    }

    public static class Address {
        private String name;
        private String phone;
        private String street;
        private String district;
        private String city;
        private String ward;

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

        public String getWard() {
            return ward;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

    }

    public static class Parcel {
        private double cod;
        private double amount;
        private int width = 10;
        private int height = 10;
        private int length = 10;
        private int weight = 220;

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
