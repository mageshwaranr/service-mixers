package com.servicemixers.demo.aws.inventory;

public class EC2Price {
    private String type;
    private Double price;

    public EC2Price(String type, Double price) {
        this.type = type;
        this.price = price;
    }

    public EC2Price() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
