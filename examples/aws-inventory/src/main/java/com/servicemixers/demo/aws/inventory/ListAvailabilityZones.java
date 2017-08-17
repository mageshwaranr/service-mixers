package com.servicemixers.demo.aws.inventory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListAvailabilityZones {

    private static final String REGION_FIELD_NAME = "region";

    public List<String> findByCriteria(Map<String, String> criteria) {
        String region = criteria.get(REGION_FIELD_NAME);
        return IntStream.range(1, 5).mapToObj(operand -> region + "-" + operand).collect(Collectors.toList());
    }
}
