package com.quantityMeasurementApp.service;

import com.quantityMeasurementApp.dto.QuantityDTO;

public interface IQuantityMeasurementService {

    QuantityDTO compare(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityDTO convert(QuantityDTO quantity, String targetUnit);

    QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit);

    QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit);

    QuantityDTO divide(QuantityDTO quantity1, QuantityDTO quantity2);

    public static void main(String[] args) {
        System.out.println("Testing IQuantityMeasurementService interface");
    }
}