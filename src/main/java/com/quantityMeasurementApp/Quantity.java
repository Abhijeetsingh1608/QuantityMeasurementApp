package com.quantityMeasurementApp;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {

	private static final double EPSILON = 1e-6;

	private final double value;
	private final U unit;

	public Quantity(double value, U unit) {

		if (unit == null) {
			throw new IllegalArgumentException("Unit cannot be null");
		}

		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be finite");
		}

		this.value = value;
		this.unit = unit;
	}

	public double getValue() {
		return value;
	}

	public U getUnit() {
		return unit;
	}

	public double toBaseUnit() {
		return unit.convertToBaseUnit(value);
	}

	public static <T extends IMeasurable> double convert(double value, T fromUnit, T toUnit) {

		if (fromUnit == null || toUnit == null) {
			throw new IllegalArgumentException("Units cannot be null");
		}

		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be finite");
		}

		double baseValue = fromUnit.convertToBaseUnit(value);
		return toUnit.convertFromBaseUnit(baseValue);
	}

	public Quantity<U> convertTo(U targetUnit) {

		if (targetUnit == null) {
			throw new IllegalArgumentException("Target unit cannot be null");
		}

		double baseValue = this.unit.convertToBaseUnit(this.value);
		double targetValue = targetUnit.convertFromBaseUnit(baseValue);

		return new Quantity<>(targetValue, targetUnit);
	}

	public Quantity<U> add(Quantity<U> other) {

		if (other == null) {
			throw new IllegalArgumentException("Quantity cannot be null");
		}

		return add(other, this.unit);
	}

	public Quantity<U> add(Quantity<U> other, U targetUnit) {

		if (other == null) {
			throw new IllegalArgumentException("Quantity cannot be null");
		}

		if (targetUnit == null) {
			throw new IllegalArgumentException("Target unit cannot be null");
		}

		double base1 = this.unit.convertToBaseUnit(this.value);
		double base2 = other.unit.convertToBaseUnit(other.value);

		double sumBase = base1 + base2;
		double resultValue = targetUnit.convertFromBaseUnit(sumBase);

		return new Quantity<>(resultValue, targetUnit);
	}

	private void validateArithmetic(Quantity<U> other) {
		if (other == null)
			throw new IllegalArgumentException("Other quantity cannot be null");

		if (this.unit == null || other.unit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		if (!this.unit.getClass().equals(other.unit.getClass()))
			throw new IllegalArgumentException("Incompatible measurement categories");

		if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
			throw new IllegalArgumentException("Values must be finite");
	}

	public Quantity<U> subtract(Quantity<U> other) {

		validateArithmetic(other);

		double baseThis = this.unit.convertToBaseUnit(this.value);
		double baseOther = other.unit.convertToBaseUnit(other.value);

		double baseResult = baseThis - baseOther;

		double resultInTarget = this.unit.convertFromBaseUnit(baseResult);

		resultInTarget = Math.round(resultInTarget * 100.0) / 100.0;

		return new Quantity<>(resultInTarget, this.unit);
	}

	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

		validateArithmetic(other);

		if (targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");

		double baseThis = this.unit.convertToBaseUnit(this.value);
		double baseOther = other.unit.convertToBaseUnit(other.value);

		double baseResult = baseThis - baseOther;

		double resultInTarget = targetUnit.convertFromBaseUnit(baseResult);

		resultInTarget = Math.round(resultInTarget * 100.0) / 100.0;

		return new Quantity<>(resultInTarget, targetUnit);
	}

	public double divide(Quantity<U> other) {

		validateArithmetic(other);

		double baseThis = this.unit.convertToBaseUnit(this.value);
		double baseOther = other.unit.convertToBaseUnit(other.value);

		if (baseOther == 0.0)
			throw new ArithmeticException("Division by zero");

		return baseThis / baseOther;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Quantity))
			return false;

		Quantity<?> that = (Quantity<?>) obj;

		if (this.unit.getClass() != that.unit.getClass())
			return false;

		double thisBase = this.unit.convertToBaseUnit(this.value);
		double thatBase = that.unit.convertToBaseUnit(that.value);

		return Math.abs(thisBase - thatBase) < EPSILON;
	}

	@Override
	public int hashCode() {
		double baseValue = unit.convertToBaseUnit(value);
		return Objects.hash(baseValue, unit.getClass());
	}

	@Override
	public String toString() {
		return String.format("%.2f %s", value, unit.getUnitName());
	}

}