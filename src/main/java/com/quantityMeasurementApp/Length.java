package com.quantityMeasurementApp;

import java.util.Objects;

public class Length {

	private final double value;
	private final LengthUnit unit;

	public enum LengthUnit {

		FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);

		private final double conversionFactor;

		LengthUnit(double conversionFactor) {
			this.conversionFactor = conversionFactor;
		}

		public double getConversionFactor() {
			return conversionFactor;
		}
	}

	public Length(double value, LengthUnit unit) {
		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be a finite number");
		}
		if (unit == null) {
			throw new IllegalArgumentException("Unit must not be null");
		}
		this.value = value;
		this.unit = unit;
	}

	private double convertToBaseUnit() {
		double valueInInches = value * unit.getConversionFactor();
		return Math.round(valueInInches * 100.0) / 100.0;
	}

	private boolean compare(Length thatLength) {
		return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
	}

	private double convertFromBaseToTargetUnit(double lengthInInches, LengthUnit targetUnit) {
		double convertedValue = lengthInInches / targetUnit.getConversionFactor();
		return Math.round(convertedValue * 100.0) / 100.0;
	}

	public Length convertTo(LengthUnit targetUnit) {
		if (targetUnit == null) {
			throw new IllegalArgumentException("Target unit must not be null");
		}

		double valueInInches = this.convertToBaseUnit();
		double roundedValue = convertFromBaseToTargetUnit(valueInInches, targetUnit);

		return new Length(roundedValue, targetUnit);
	}

	public static double convert(double value, LengthUnit source, LengthUnit target) {

		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be a finite number");
		}

		if (source == null || target == null) {
			throw new IllegalArgumentException("Source and target units must not be null");
		}

		double valueInInches = value * source.getConversionFactor();
		double result = valueInInches / target.getConversionFactor();

		return Math.round(result * 100.0) / 100.0;
	}

	public Length add(Length thatLength) {

		if (thatLength == null) {
			throw new IllegalArgumentException("Length to add must not be null");
		}

		double thisInches = this.convertToBaseUnit();
		double thatInches = thatLength.convertToBaseUnit();

		double sumInches = thisInches + thatInches;

		double resultValue = convertFromBaseToTargetUnit(sumInches, this.unit);

		return new Length(resultValue, this.unit);
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;

		Length other = (Length) o;
		return compare(other);
	}

	@Override
	public int hashCode() {
		return Objects.hash(convertToBaseUnit());
	}

	@Override
	public String toString() {
		return String.format("%.2f %s", value, unit);
	}
}