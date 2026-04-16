package com.app.measurementservice.dto;

import com.app.measurementservice.domain.MeasurementCategory;
import com.app.measurementservice.domain.MeasurementOperation;

import java.time.Instant;

public record MeasurementResponse(
        Long recordId,
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
}
