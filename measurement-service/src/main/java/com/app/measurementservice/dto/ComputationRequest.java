package com.app.measurementservice.dto;

import com.app.measurementservice.domain.MeasurementCategory;
import com.app.measurementservice.domain.MeasurementOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComputationRequest(
        Long userId,
        @NotNull MeasurementCategory category,
        @NotNull MeasurementOperation operation,
        @NotNull Double leftValue,
        @NotBlank String leftUnit,
        @NotNull Double rightValue,
        @NotBlank String rightUnit,
        String resultUnit
) {
}
