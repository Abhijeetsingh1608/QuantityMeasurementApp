package com.quantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(3.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(36.0, LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(
				new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(0.393701, LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_YardToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.YARDS).equals(new Length(2.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_CentimetersToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(1.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_MultiUnit_TransitiveProperty() {
		Length yard = new Length(1.0, LengthUnit.YARDS);
		Length feet = new Length(3.0, LengthUnit.FEET);
		Length inches = new Length(36.0, LengthUnit.INCHES);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yard.equals(inches));
	}

	@Test
	public void testEquality_SameReference() {
		Length length = new Length(2.0, LengthUnit.YARDS);
		assertTrue(length.equals(length));
	}

	@Test
	public void testEquality_NullComparison() {
		Length length = new Length(2.0, LengthUnit.YARDS);
		assertFalse(length.equals(null));
	}

	@Test
	public void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Length(1.0, null));
	}

	@Test
	public void testConversion_FeetToInches() {
		double result = Length.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(12.0, result);
	}

	@Test
	public void testConversion_InchesToFeet() {
		double result = Length.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
		assertEquals(2.0, result);
	}

	@Test
	public void testConversion_YardsToInches() {
		double result = Length.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
		assertEquals(36.0, result);
	}

	@Test
	public void testConversion_CentimetersToInches() {
		double result = Length.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
		assertEquals(1.0, result, 1e-6);
	}

	@Test
	public void testConversion_ZeroValue() {
		double result = Length.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(0.0, result);
	}

	@Test
	public void testConversion_NegativeValue() {
		double result = Length.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(-12.0, result);
	}

	@Test
	public void testConversion_SameUnit() {
		double result = Length.convert(5.0, LengthUnit.FEET, LengthUnit.FEET);
		assertEquals(5.0, result);
	}

	@Test
	public void testConversion_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> Length.convert(1.0, null, LengthUnit.FEET));
	}

	@Test
	public void testConversion_NaN() {
		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES));
	}

	@Test
	public void testConversion_Infinite() {
		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES));
	}

	@Test
	public void testLengthUnit_ConvertToBaseUnit() {
		assertEquals(144.0, LengthUnit.FEET.convertToBaseUnit(12.0), 1e-9);
		assertEquals(12.0, LengthUnit.INCHES.convertToBaseUnit(12.0), 1e-9);
		assertEquals(36.0, LengthUnit.YARDS.convertToBaseUnit(1.0), 1e-9);
		assertEquals(12.0, LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), 1e-2);
	}

	@Test
	public void testLengthUnit_ConvertFromBaseUnit() {
		assertEquals(1.0, LengthUnit.FEET.convertFromBaseUnit(12.0), 1e-9);
		assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(12.0), 1e-9);
		assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(36.0), 1e-9);
		assertEquals(30.48, LengthUnit.CENTIMETERS.convertFromBaseUnit(12.0), 1e-2);
	}

	@Test
	public void testAddition_SameUnit_FeetPlusFeet() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(2.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(3.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_SameUnit_InchPlusInch() {

		Length l1 = new Length(6.0, LengthUnit.INCHES);
		Length l2 = new Length(6.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(new Length(12.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_CrossUnit_FeetPlusInches() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(new Length(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_CrossUnit_InchPlusFeet() {

		Length l1 = new Length(12.0, LengthUnit.INCHES);
		Length l2 = new Length(1.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(24.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_CrossUnit_YardPlusFeet() {

		Length l1 = new Length(1.0, LengthUnit.YARDS);
		Length l2 = new Length(3.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(2.0, LengthUnit.YARDS), result);
	}

	@Test
	public void testAddition_CrossUnit_CentimeterPlusInch() {

		Length l1 = new Length(2.54, LengthUnit.CENTIMETERS);
		Length l2 = new Length(1.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertTrue(result.equals(new Length(5.08, LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testAddition_Commutativity() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		assertTrue(l1.add(l2).equals(l2.add(l1)));
	}

	@Test
	public void testAddition_WithZero() {

		Length l1 = new Length(5.0, LengthUnit.FEET);
		Length l2 = new Length(0.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(new Length(5.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_NegativeValues() {

		Length l1 = new Length(5.0, LengthUnit.FEET);
		Length l2 = new Length(-2.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(3.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_NullSecondOperand() {

		Length l1 = new Length(1.0, LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> l1.add(null));
	}

	@Test
	public void testAddition_LargeValues() {

		Length l1 = new Length(1e6, LengthUnit.FEET);
		Length l2 = new Length(1e6, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(2e6, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_SmallValues() {

		Length l1 = new Length(0.01, LengthUnit.FEET);
		Length l2 = new Length(0.02, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(0.03, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Feet() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2, LengthUnit.FEET);

		assertEquals(new Length(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Inches() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2, LengthUnit.INCHES);

		assertEquals(new Length(24.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Yards() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2, LengthUnit.YARDS);

		assertTrue(result.equals(new Length(0.67, LengthUnit.YARDS)));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_NullTargetUnit() {

		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		assertThrows(IllegalArgumentException.class, () -> l1.add(l2, null));
	}

	@Test
	public void testLengthUnitEnum_FeetConstant() {
		assertEquals(12.0, LengthUnit.FEET.getConversionFactor(), 0.01);
	}

	@Test
	public void testLengthUnitEnum_InchesConstant() {
		assertEquals(1.0, LengthUnit.INCHES.getConversionFactor(), 0.01);
	}

	@Test
	public void testLengthUnitEnum_YardsConstant() {
		assertEquals(36.0, LengthUnit.YARDS.getConversionFactor(), 0.01);
	}

	@Test
	public void testLengthUnitEnum_CentimetersConstant() {
		assertEquals(0.393701, LengthUnit.CENTIMETERS.getConversionFactor(), 0.01);
	}

	@Test
	public void testConvertToBaseUnit_FeetToInches() {
		assertEquals(12.0, LengthUnit.FEET.convertToBaseUnit(1.0), 0.01);
	}

	@Test
	public void testConvertToBaseUnit_InchesToInches() {
		assertEquals(12.0, LengthUnit.INCHES.convertToBaseUnit(12.0), 0.01);
	}

	@Test
	public void testConvertToBaseUnit_YardsToInches() {
		assertEquals(36.0, LengthUnit.YARDS.convertToBaseUnit(1.0), 0.01);
	}

	@Test
	public void testConvertToBaseUnit_CentimetersToInches() {
		assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(2.54), 0.01);
	}

	@Test
	public void testConvertFromBaseUnit_InchesToFeet() {
		assertEquals(1.0, LengthUnit.FEET.convertFromBaseUnit(12.0), 0.01);
	}

	@Test
	public void testConvertFromBaseUnit_InchesToInches() {
		assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(12.0), 0.01);
	}

	@Test
	public void testConvertFromBaseUnit_InchesToYards() {
		assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(36.0), 0.01);
	}

	@Test
	public void testConvertFromBaseUnit_InchesToCentimeters() {
		assertEquals(2.54, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), 0.01);
	}

	@Test
	public void testQuantityLengthRefactored_Equality() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		assertTrue(l1.equals(l2));
	}

	@Test
	public void testQuantityLengthRefactored_Add() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2, LengthUnit.FEET);

		assertEquals(new Length(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testQuantityLengthRefactored_AddWithTargetUnit() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2, LengthUnit.YARDS);

		assertTrue(result.equals(new Length(0.67, LengthUnit.YARDS)));
	}

	@Test
	public void testQuantityLengthRefactored_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Length(1.0, null));
	}

	@Test
	public void testQuantityLengthRefactored_InvalidValue() {
		assertThrows(IllegalArgumentException.class, () -> new Length(Double.NaN, LengthUnit.FEET));
	}

	@Test
	public void testRoundTripConversion_RefactoredDesign() {
		double original = 5.0;
		double toInches = LengthUnit.FEET.convertToBaseUnit(original);
		double backToFeet = LengthUnit.FEET.convertFromBaseUnit(toInches);

		assertEquals(original, backToFeet, 0.01);
	}
}