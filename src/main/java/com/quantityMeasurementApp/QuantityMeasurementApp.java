package com.quantityMeasurementApp;

public class QuantityMeasurementApp {

	public static boolean demonstrateLengthEquality(Length l1, Length l2) {
		return l1.equals(l2);
	}

	public static boolean demonstrateLengthComparison(double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2) {

		Length l1 = new Length(value1, unit1);
		Length l2 = new Length(value2, unit2);

		boolean result = l1.equals(l2);

		System.out.println("lengths are equal : " + result);
		return result;
	}

	public static double demonstrateLengthConversion(double value, Length.LengthUnit from, Length.LengthUnit to) {

		double result = Length.convert(value, from, to);

		System.out.println(value + " " + from + " = " + result + " " + to);

		return result;
	}

	public static Length demonstrateLengthConversion(Length length, Length.LengthUnit toUnit) {

		Length result = length.convertTo(toUnit);

		System.out.println(length + " = " + result);

		return result;
	}

	public static void main(String[] args) {

		demonstrateLengthComparison(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES);

		demonstrateLengthComparison(1.0, Length.LengthUnit.YARDS, 3.0, Length.LengthUnit.FEET);

		demonstrateLengthComparison(1.0, Length.LengthUnit.YARDS, 36.0, Length.LengthUnit.INCHES);

		demonstrateLengthComparison(1.0, Length.LengthUnit.CENTIMETERS, 0.393701, Length.LengthUnit.INCHES);

		demonstrateLengthComparison(2.0, Length.LengthUnit.YARDS, 6.0, Length.LengthUnit.FEET);

		demonstrateLengthConversion(1.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);

		demonstrateLengthConversion(3.0, Length.LengthUnit.YARDS, Length.LengthUnit.FEET);

		demonstrateLengthConversion(2.54, Length.LengthUnit.CENTIMETERS, Length.LengthUnit.INCHES);

		Length lengthInYards = new Length(2.0, Length.LengthUnit.YARDS);
		demonstrateLengthConversion(lengthInYards, Length.LengthUnit.INCHES);

		Length lengthInFeet = new Length(3.0, Length.LengthUnit.FEET);
		demonstrateLengthConversion(lengthInFeet, Length.LengthUnit.YARDS);
	}
}