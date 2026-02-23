package com.quantityMeasurementApp;

import java.util.Objects;

public class Weight {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final WeightUnit unit;

    public Weight(double value, WeightUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public static double convert(double value, WeightUnit source, WeightUnit target) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        if (source == null || target == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        double baseValue = source.convertToBaseUnit(value);

        return target.convertFromBaseUnit(baseValue);
    }

    public Weight convertTo(WeightUnit targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = this.unit.convertToBaseUnit(this.value);
        double targetValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Weight(targetValue, targetUnit);
    }

    public Weight add(Weight other) {

        if (other == null) {
            throw new IllegalArgumentException("Weight cannot be null");
        }

        return add(other, this.unit);
    }

    public Weight add(Weight other, WeightUnit targetUnit) {

        if (other == null) {
            throw new IllegalArgumentException("Weight cannot be null");
        }

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double sumBase = base1 + base2;

        double resultValue = targetUnit.convertFromBaseUnit(sumBase);

        return new Weight(resultValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Weight))
            return false;

        Weight other = (Weight) obj;

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) <= EPSILON;
    }

    @Override
    public int hashCode() {
        long normalized = Math.round(toBaseUnit() / EPSILON);
        return Objects.hash(normalized);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }
}