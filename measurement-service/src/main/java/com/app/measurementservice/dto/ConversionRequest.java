package com.app.measurementservice.dto;

import com.app.measurementservice.domain.MeasurementCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConversionRequest(
        Long userId,
        @NotNull MeasurementCategory category,
        @NotNull Double value,
        @NotBlank String fromUnit,
        @NotBlank String toUnit
) {
}
