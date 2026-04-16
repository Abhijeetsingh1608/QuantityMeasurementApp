package com.app.measurementservice.service;

import com.app.measurementservice.domain.MeasurementCategory;
import com.app.measurementservice.domain.MeasurementOperation;
import org.springframework.stereotype.Component;

@Component
public class MeasurementEngine {

    private static final double KM_TO_MILES = 0.621371;
    private static final double KG_TO_LBS = 2.20462;
    private static final double LITRE_TO_GALLONS = 0.264172;
    private static final double EPSILON = 1e-9;

    public double convert(MeasurementCategory category, double value, String fromUnit, String toUnit) {
        return round(convertFromCanonical(category, toCanonical(category, value, fromUnit), toUnit));
    }

    public ComputationResult compute(
            MeasurementCategory category,
            MeasurementOperation operation,
            double leftValue,
            String leftUnit,
            double rightValue,
            String rightUnit,
            String resultUnit
    ) {
        if (operation == MeasurementOperation.CONVERT) {
            throw new IllegalArgumentException("Use the convert endpoint for conversion requests");
        }
        if (category == MeasurementCategory.TEMPERATURE && operation != MeasurementOperation.COMPARE) {
            throw new IllegalArgumentException("Temperature supports compare only in this service");
        }

        double leftCanonical = toCanonical(category, leftValue, leftUnit);
        double rightCanonical = toCanonical(category, rightValue, rightUnit);

        return switch (operation) {
            case ADD -> numericResult(category, leftCanonical + rightCanonical, resultUnit);
            case SUBTRACT -> numericResult(category, leftCanonical - rightCanonical, resultUnit);
            case DIVIDE -> new ComputationResult(
                    null,
                    round(divide(leftCanonical, rightCanonical)),
                    null,
                    "Division completed in canonical units"
            );
            case COMPARE -> new ComputationResult(
                    null,
                    null,
                    Math.abs(leftCanonical - rightCanonical) < EPSILON,
                    "Comparison completed"
            );
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }

    private ComputationResult numericResult(MeasurementCategory category, double canonicalValue, String resultUnit) {
        String safeResultUnit = resolveResultUnit(category, resultUnit);
        return new ComputationResult(
                safeResultUnit,
                round(convertFromCanonical(category, canonicalValue, safeResultUnit)),
                null,
                "Computation completed"
        );
    }

    private String resolveResultUnit(MeasurementCategory category, String requestedUnit) {
        if (requestedUnit == null || requestedUnit.isBlank()) {
            return switch (category) {
                case LENGTH -> "KM";
                case WEIGHT -> "KG";
                case VOLUME -> "L";
                case TEMPERATURE -> "C";
            };
        }
        return requestedUnit.toUpperCase();
    }

    private double toCanonical(MeasurementCategory category, double value, String unit) {
        String normalizedUnit = unit.toUpperCase();
        return switch (category) {
            case LENGTH -> switch (normalizedUnit) {
                case "KM" -> value;
                case "MILES" -> value / KM_TO_MILES;
                default -> throw unsupported(category, unit);
            };
            case WEIGHT -> switch (normalizedUnit) {
                case "KG" -> value;
                case "LBS" -> value / KG_TO_LBS;
                default -> throw unsupported(category, unit);
            };
            case VOLUME -> switch (normalizedUnit) {
                case "L" -> value;
                case "GALLONS" -> value / LITRE_TO_GALLONS;
                default -> throw unsupported(category, unit);
            };
            case TEMPERATURE -> switch (normalizedUnit) {
                case "C" -> value;
                case "F" -> (value - 32) * 5 / 9;
                default -> throw unsupported(category, unit);
            };
        };
    }

    private double convertFromCanonical(MeasurementCategory category, double value, String unit) {
        String normalizedUnit = unit.toUpperCase();
        return switch (category) {
            case LENGTH -> switch (normalizedUnit) {
                case "KM" -> value;
                case "MILES" -> value * KM_TO_MILES;
                default -> throw unsupported(category, unit);
            };
            case WEIGHT -> switch (normalizedUnit) {
                case "KG" -> value;
                case "LBS" -> value * KG_TO_LBS;
                default -> throw unsupported(category, unit);
            };
            case VOLUME -> switch (normalizedUnit) {
                case "L" -> value;
                case "GALLONS" -> value * LITRE_TO_GALLONS;
                default -> throw unsupported(category, unit);
            };
            case TEMPERATURE -> switch (normalizedUnit) {
                case "C" -> value;
                case "F" -> (value * 9 / 5) + 32;
                default -> throw unsupported(category, unit);
            };
        };
    }

    private IllegalArgumentException unsupported(MeasurementCategory category, String unit) {
        return new IllegalArgumentException("Unsupported unit '" + unit + "' for category " + category);
    }

    private double divide(double left, double right) {
        if (Math.abs(right) < EPSILON) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return left / right;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public record ComputationResult(
            String resultUnit,
            Double resultValue,
            Boolean comparisonResult,
            String message
    ) {
    }
}
