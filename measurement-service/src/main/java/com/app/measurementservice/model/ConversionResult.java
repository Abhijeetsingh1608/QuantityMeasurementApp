package com.app.measurementservice.model;

public record ConversionResult(
        String from,
        String to,
        Double input,
        Double result
) {
}
