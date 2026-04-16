package com.app.userservice.dto;

import com.app.userservice.domain.MeasurementCategory;
import com.app.userservice.domain.MeasurementOperation;

import java.time.Instant;

public record UserHistoryResponse(
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
}
