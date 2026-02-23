package com.quantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(1.0, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToYard_DifferentValue() {
		assertFalse(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(2.0, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(3.0, Length.LengthUnit.FEET)));
	}

	@Test
	public void testEquality_FeetToYard_EquivalentValue() {
		assertTrue(new Length(3.0, Length.LengthUnit.FEET).equals(new Length(1.0, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(36.0, Length.LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_InchesToYard_EquivalentValue() {
		assertTrue(new Length(36.0, Length.LengthUnit.INCHES).equals(new Length(1.0, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_CentimetersToCentimeters_SameValue() {
		assertTrue(
				new Length(2.0, Length.LengthUnit.CENTIMETERS).equals(new Length(2.0, Length.LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testEquality_CentimetersToCentimeters_DifferentValue() {
		assertFalse(
				new Length(2.0, Length.LengthUnit.CENTIMETERS).equals(new Length(3.0, Length.LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.CENTIMETERS).equals(new Length(0.393701, Length.LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_InchesToCentimeters_EquivalentValue() {
		assertTrue(new Length(0.393701, Length.LengthUnit.INCHES).equals(new Length(1.0, Length.LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testEquality_YardToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(2.0, Length.LengthUnit.FEET)));
	}

	@Test
	public void testEquality_CentimetersToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, Length.LengthUnit.CENTIMETERS).equals(new Length(1.0, Length.LengthUnit.FEET)));
	}

	@Test
	public void testEquality_MultiUnit_TransitiveProperty() {
		Length yard = new Length(1.0, Length.LengthUnit.YARDS);
		Length feet = new Length(3.0, Length.LengthUnit.FEET);
		Length inches = new Length(36.0, Length.LengthUnit.INCHES);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yard.equals(inches));
	}

	@Test
	public void testEquality_AllUnits_ComplexScenario() {
		Length yards = new Length(2.0, Length.LengthUnit.YARDS);
		Length feet = new Length(6.0, Length.LengthUnit.FEET);
		Length inches = new Length(72.0, Length.LengthUnit.INCHES);

		assertTrue(yards.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yards.equals(inches));
	}

	@Test
	public void testEquality_SameReference() {
		Length length = new Length(2.0, Length.LengthUnit.YARDS);
		assertTrue(length.equals(length));
	}

	@Test
	public void testEquality_NullComparison() {
		Length length = new Length(2.0, Length.LengthUnit.YARDS);
		assertFalse(length.equals(null));
	}

	@Test
	public void testEquality_DifferentClass() {
		Length length = new Length(2.0, Length.LengthUnit.YARDS);
		assertFalse(length.equals("2.0 YARDS"));
	}

	@Test
	public void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Length(1.0, null));
	}

	@Test
	public void testEquality_NonNumericInput_NaN() {
		assertThrows(IllegalArgumentException.class, () -> new Length(Double.NaN, Length.LengthUnit.FEET));
	}

	@Test
	public void testEquality_NonNumericInput_Infinite() {
		assertThrows(IllegalArgumentException.class,
				() -> new Length(Double.POSITIVE_INFINITY, Length.LengthUnit.CENTIMETERS));
	}

	@Test
	public void testEquality_DemonstrateLengthComparisonMethod() {
		assertTrue(QuantityMeasurementApp.demonstrateLengthComparison(1.0, Length.LengthUnit.YARDS, 36.0,
				Length.LengthUnit.INCHES));
	}
}