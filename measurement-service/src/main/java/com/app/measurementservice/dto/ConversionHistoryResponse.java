package com.app.measurementservice.dto;

import com.app.measurementservice.domain.MeasurementCategory;
import com.app.measurementservice.domain.MeasurementOperation;

import java.time.Instant;

public record ConversionHistoryResponse(
        Long id,
        Long userId,
        MeasurementCategory category,
        MeasurementOperation operation,
        Double inputValue,
        String inputUnit,
        Double secondaryValue,
        String secondaryUnit,
        Double resultValue,
        String resultUnit,
        Boolean comparisonResult,
        String message,
        Instant recordedAt
) {

    public static ConversionHistoryResponse empty() {
        return new ConversionHistoryResponse(
                null, null, null, null, null, null, null, null, null, null, null, "History not saved", null
        );
    }
}
