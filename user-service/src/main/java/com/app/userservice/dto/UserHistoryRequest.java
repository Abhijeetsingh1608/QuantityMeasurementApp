package com.app.userservice.dto;

import com.app.userservice.domain.MeasurementCategory;
import com.app.userservice.domain.MeasurementOperation;
import jakarta.validation.constraints.NotNull;

public record UserHistoryRequest(
        @NotNull MeasurementCategory category,
        @NotNull MeasurementOperation operation,
        @NotNull Double inputValue,
        String inputUnit,
        Double secondaryValue,
        String secondaryUnit,
        Double resultValue,
        String resultUnit,
        Boolean comparisonResult,
        String message
) {
}
