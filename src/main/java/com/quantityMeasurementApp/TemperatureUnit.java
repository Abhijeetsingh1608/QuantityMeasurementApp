package com.quantityMeasurementApp;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {

	CELSIUS(c -> c), 
	FAHRENHEIT(f -> (f - 32) * 5 / 9), 
	KELVIN(k -> k - 273.15);

	private final Function<Double, Double> conversionToBase;

	TemperatureUnit(Function<Double, Double> conversionToBase) {
		this.conversionToBase = conversionToBase;
	}

	@Override
	public String getUnitName() {
		return name();
	}

	@Override
	public double getConversionFactor() {
		return 1.0;
	}

	@Override
	public double convertToBaseUnit(double value) {
		return conversionToBase.apply(value);
	}

	@Override
	public double convertFromBaseUnit(double baseValue) {

		switch (this) {
		case FAHRENHEIT:
			return (baseValue * 9 / 5) + 32;
		case KELVIN:
			return baseValue + 273.15;
		default:
			return baseValue;
		}
	}

	private final SupportsArithmetic supportsArithmetic = () -> false;

	@Override
	public boolean supportsArithmetic() {
		return supportsArithmetic.isSupported();
	}

	@Override
	public void validateOperationSupport(String operation) {
		throw new UnsupportedOperationException(
				"Temperature does not support operation: " + operation + " on absolute values.");
	}
}