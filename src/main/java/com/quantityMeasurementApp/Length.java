package com.quantityMeasurementApp;

import java.util.Objects;

public class Length {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {

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

    public static double convert(double value, LengthUnit source, LengthUnit target) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        if (source == null || target == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        double baseValue = source.convertToBaseUnit(value);

        return target.convertFromBaseUnit(baseValue);
    }

    public Length add(Length other) {

        if (other == null) {
            throw new IllegalArgumentException("Length cannot be null");
        }

        return add(other, this.unit);
    }

    public Length add(Length other, LengthUnit targetUnit) {

        if (other == null) {
            throw new IllegalArgumentException("Length cannot be null");
        }

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double sumBase = base1 + base2;

        double resultValue = targetUnit.convertFromBaseUnit(sumBase);

        return new Length(resultValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Length))
            return false;

        Length other = (Length) obj;

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