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

	public static boolean demonstrateWeightEquality(Weight w1, Weight w2) {
		return w1.equals(w2);
	}

	public static boolean demonstrateWeightComparison(double value1, WeightUnit unit1, double value2,
			WeightUnit unit2) {

		Weight w1 = new Weight(value1, unit1);
		Weight w2 = new Weight(value2, unit2);

		boolean result = w1.equals(w2);

		System.out.println("weights are equal : " + result);
		return result;
	}

	public static double demonstrateWeightConversion(double value, WeightUnit from, WeightUnit to) {

		double result = Weight.convert(value, from, to);

		System.out.println(value + " " + from + " = " + result + " " + to);

		return result;
	}

	public static Weight demonstrateWeightAddition(Weight w1, Weight w2) {

		Weight result = w1.add(w2);

		System.out.println("Addition : " + result);

		return result;
	}

	public static Weight demonstrateWeightAddition(Weight w1, Weight w2, WeightUnit targetUnit) {

		Weight result = w1.add(w2, targetUnit);

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

		System.out.println();

		demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);

		demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 2.20462, WeightUnit.POUND);

		demonstrateWeightComparison(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM);

		demonstrateWeightConversion(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);

		demonstrateWeightConversion(2.0, WeightUnit.POUND, WeightUnit.KILOGRAM);

		demonstrateWeightConversion(500.0, WeightUnit.GRAM, WeightUnit.POUND);

		demonstrateWeightAddition(new Weight(1.0, WeightUnit.KILOGRAM), new Weight(2.0, WeightUnit.KILOGRAM));

		demonstrateWeightAddition(new Weight(1.0, WeightUnit.KILOGRAM), new Weight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);
	}
}