package com.quantityMeasurementApp;

public interface IMeasurable {

	double getConversionFactor();

	double convertToBaseUnit(double value);

	double convertFromBaseUnit(double baseValue);

	String getUnitName();

	String getMeasurementType();

	static IMeasurable getUnitByName(String unitName, String measurementType) {
		switch (measurementType) {
			case "LengthUnit":
				return LengthUnit.valueOf(unitName);
			case "WeightUnit":
				return WeightUnit.valueOf(unitName);
			case "VolumeUnit":
				return VolumeUnit.valueOf(unitName);
			case "TemperatureUnit":
				return TemperatureUnit.valueOf(unitName);
			default:
				throw new IllegalArgumentException("Unknown measurement type: " + measurementType);
		}
	}

	SupportsArithmetic supportsArithmetic = () -> true;

	default boolean supportsArithmetic() {
		return supportsArithmetic.isSupported();
	}

	default void validateOperationSupport(String operation) {
	}
}