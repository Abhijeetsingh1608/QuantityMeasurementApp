package com.quantityMeasurementApp;

import java.util.Objects;

public class Length {

	private final double value;
	private final LengthUnit unit;

	public enum LengthUnit {

		FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);

		private final double toInchesFactor;

		LengthUnit(double toInchesFactor) {
			this.toInchesFactor = toInchesFactor;
		}

		public double toInches(double value) {
			return value * toInchesFactor;
		}
	}

	public Length(double value, LengthUnit unit) {
		if (unit == null) {
			throw new IllegalArgumentException("Unit cannot be null");
		}
		this.value = value;
		this.unit = unit;
	}

	private double toBaseUnit() {
		double valueInInches = unit.toInches(value);
		return Math.round(valueInInches * 100.0) / 100.0;
	}

	public static double convert(double value, LengthUnit source, LengthUnit target) {

		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value is finite");
		}

		if (source == null || target == null) {
			throw new IllegalArgumentException("not null unit");
		}

		double valueInInches = source.toInches(value);

		double result = valueInInches / target.toInches(1.0);

		return Math.round(result * 100.0) / 100.0;
	}

	public Length add(Length other) {

		if (other == null) {
			throw new IllegalArgumentException("Length cannot be null");
		}

		double thisInches = this.toBaseUnit();
		double otherInches = other.toBaseUnit();

		double sumInches = thisInches + otherInches;

		double resultValue = sumInches / this.unit.toInches(1.0);

		return new Length(resultValue, this.unit);
	}

	public Length add(Length other, LengthUnit targetUnit) {

		if (other == null) {
			throw new IllegalArgumentException("length cannot be null");
		}

		if (targetUnit == null) {
			throw new IllegalArgumentException("target unit not be null");
		}

		if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
			throw new IllegalArgumentException("finite value only");
		}

		double thisInches = this.toBaseUnit();
		double otherInches = other.toBaseUnit();

		double sumInches = thisInches + otherInches;

		double resultValue = sumInches / targetUnit.toInches(1.0);

		return new Length(resultValue, targetUnit);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Length other = (Length) obj;

		return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(toBaseUnit());
	}

	@Override
	public String toString() {
		return String.format("%.2f %s", value, unit);
	}
}