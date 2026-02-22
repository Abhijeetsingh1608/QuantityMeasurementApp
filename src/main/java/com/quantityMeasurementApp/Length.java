package com.quantityMeasurementApp;

import java.util.Objects;

public class Length {

	private final double value;
	private final LengthUnit unit;

	public enum LengthUnit {
		FEET(1.0), INCHES(1.0 / 12.0);

		private final double toFeetFactor;

		LengthUnit(double toFeetFactor) {
			this.toFeetFactor = toFeetFactor;
		}

		public double toFeet(double value) {
			return value * toFeetFactor;
		}
	}

	public Length(double value, LengthUnit unit) {
		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("value must be a finite number");
		}
		if (unit == null) {
			throw new IllegalArgumentException("unit must not be null");
		}
		this.value = value;
		this.unit = unit;
	}

	private double toBaseUnit() {
		return unit.toFeet(value);
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
}