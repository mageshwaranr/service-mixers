package com.servicemixers.demo.aws.inventory;

import java.util.ArrayList;
import java.util.List;

public class ListEc2InstancePrices {

    public List<EC2Price> listEc2PricesByZones(String[] zones) {
        List<EC2Price> prices = new ArrayList<>();
        prices.add(new EC2Price("m4.large",0.1));
        prices.add(new EC2Price("m4.xlarge",0.2));
        prices.add(new EC2Price("p2.xlarge",0.9));
        prices.add(new EC2Price("m4.2xlarge",0.4));
        prices.add(new EC2Price("p2.8xlarge",7.2));
        prices.add(new EC2Price("m4.4large",0.8));
        prices.add(new EC2Price("t2.nano",0.0059));
        prices.add(new EC2Price("t2.micro",0.012));
        prices.add(new EC2Price("t2.small",0.023));
        prices.add(new EC2Price("t2.medium",0.047));
        prices.add(new EC2Price("g3.16xlarge",4.56));
        prices.add(new EC2Price("g3.4xlarge",1.14));
        prices.add(new EC2Price("g3.8xlarge",2.28));
        prices.add(new EC2Price("x1.16xlarge",6.669));
        prices.add(new EC2Price("x1.32xlarge",13.338));
        return prices;
    }
}
