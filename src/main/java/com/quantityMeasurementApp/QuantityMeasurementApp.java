package com.quantityMeasurementApp;

public class QuantityMeasurementApp {

	public static boolean demonstrateLengthEquality(Length l1, Length l2) {
		return l1.equals(l2);
	}

	public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {

		Length l1 = new Length(value1, unit1);
		Length l2 = new Length(value2, unit2);

		boolean result = l1.equals(l2);

		System.out.println("lengths are equal : " + result);
		return result;
	}

	public static double demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {

		double result = Length.convert(value, from, to);

		System.out.println(value + " " + from + " = " + result + " " + to);

		return result;
	}

	public static Length demonstrateLengthAddition(Length l1, Length l2) {

		Length result = l1.add(l2);

		System.out.println("Addition : " + result);

		return result;
	}

	public static Length demonstrateLengthAddition(Length l1, Length l2, LengthUnit targetUnit) {

		Length result = l1.add(l2, targetUnit);

		System.out.println("Addition : " + result);

		return result;
	}

	public static void main(String[] args) {

		demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);

		demonstrateLengthComparison(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET);

		demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES);

		demonstrateLengthComparison(1.0, LengthUnit.CENTIMETERS, 0.393701, LengthUnit.INCHES);

		demonstrateLengthComparison(2.0, LengthUnit.YARDS, 6.0, LengthUnit.FEET);

		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);

		demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);

		demonstrateLengthConversion(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);

		demonstrateLengthAddition(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCHES));
		
		demonstrateLengthAddition(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
	}
}