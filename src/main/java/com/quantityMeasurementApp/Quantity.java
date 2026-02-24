package com.quantityMeasurementApp;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

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

		if (!unit.getClass().equals(other.unit.getClass())) {
			throw new IllegalArgumentException("Incompatible measurement categories");
		}

		this.unit.validateOperationSupport("ADD");
		other.unit.validateOperationSupport("ADD");

		return add(other, this.unit);
	}

	public Quantity<U> add(Quantity<U> other, U targetUnit) {

		if (other == null) {
			throw new IllegalArgumentException("Quantity cannot be null");
		}

		if (targetUnit == null) {
			throw new IllegalArgumentException("Target unit cannot be null");
		}

		if (!unit.getClass().equals(other.unit.getClass())) {
			throw new IllegalArgumentException("Incompatible measurement categories");
		}

		this.unit.validateOperationSupport("ADD");
		other.unit.validateOperationSupport("ADD");

		double base1 = this.unit.convertToBaseUnit(this.value);
		double base2 = other.unit.convertToBaseUnit(other.value);

		double sumBase = base1 + base2;
		double resultValue = targetUnit.convertFromBaseUnit(sumBase);

		return new Quantity<>(resultValue, targetUnit);
	}



	public Quantity<U> subtract(Quantity<U> other) {

		validateArithmeticOperands(other, this.unit, false);

		this.unit.validateOperationSupport("SUBTRACT");
		other.unit.validateOperationSupport("SUBTRACT");

		double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);

		double resultInTarget = this.unit.convertFromBaseUnit(baseResult);

		resultInTarget = Math.round(resultInTarget * 100.0) / 100.0;

		return new Quantity<>(resultInTarget, this.unit);
	}

	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

		validateArithmeticOperands(other, targetUnit, true);

		this.unit.validateOperationSupport("SUBTRACT");
		other.unit.validateOperationSupport("SUBTRACT");

		double baseThis = this.unit.convertToBaseUnit(this.value);
		double baseOther = other.unit.convertToBaseUnit(other.value);

		double baseResult = baseThis - baseOther;

		double resultInTarget = targetUnit.convertFromBaseUnit(baseResult);

		resultInTarget = Math.round(resultInTarget * 100.0) / 100.0;

		return new Quantity<>(resultInTarget, targetUnit);
	}

	public double divide(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		this.unit.validateOperationSupport("DIVIDE");
		other.unit.validateOperationSupport("DIVIDE");

		double baseThis = this.unit.convertToBaseUnit(this.value);
		double baseOther = other.unit.convertToBaseUnit(other.value);

		if (Math.abs(baseOther) < EPSILON)
			throw new ArithmeticException("Division by zero");

		return baseThis / baseOther;
	}

	private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {

		if (other == null) {
			throw new IllegalArgumentException("Other quantity cannot be null");
		}

		if (!unit.getClass().equals(other.unit.getClass())) {
			throw new IllegalArgumentException("Incompatible measurement categories");
		}

		if (Double.isNaN(value) || Double.isInfinite(value) || Double.isNaN(other.value)
				|| Double.isInfinite(other.value)) {
			throw new IllegalArgumentException("Invalid numeric values");
		}

		if (targetUnitRequired && targetUnit == null) {
			throw new IllegalArgumentException("Target unit cannot be null");
		}
	}

	private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {

		double thisBase = unit.convertToBaseUnit(this.value);
		double otherBase = other.unit.convertToBaseUnit(other.value);

		return operation.compute(thisBase, otherBase);
	}

	private enum ArithmeticOperation {

		ADD((a, b) -> a + b),

		SUBTRACT((a, b) -> a - b),

		DIVIDE((a, b) -> {
			if (Math.abs(b) < EPSILON) {
				throw new ArithmeticException("Cannot divide by zero");
			}
			return a / b;
		});

		private final DoubleBinaryOperator operation;

		ArithmeticOperation(DoubleBinaryOperator operation) {
			this.operation = operation;
		}

		public double compute(double a, double b) {
			return operation.applyAsDouble(a, b);
		}
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