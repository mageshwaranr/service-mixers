package com.servicemixers.demo.aws.inventory;

public class FindEc2WithMaxPriceInARegion {

    public EC2Price findMaxEc2Price(EC2Price[] prices) {
        EC2Price maxPrice = new EC2Price("none", 0.0);
        for (EC2Price currentPrice : prices) {
            if (currentPrice.getPrice() >= maxPrice.getPrice()) {
                maxPrice = currentPrice;
            }
        }
        return maxPrice;
    }
}
