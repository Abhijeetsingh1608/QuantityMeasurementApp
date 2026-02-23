package com.quantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QuantityMeasurementAppTest {

	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(1.0, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(3.0, Length.LengthUnit.FEET)));
	}

	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Length(1.0, Length.LengthUnit.YARDS).equals(new Length(36.0, Length.LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(
				new Length(1.0, Length.LengthUnit.CENTIMETERS).equals(new Length(0.393701, Length.LengthUnit.INCHES)));
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
	public void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Length(1.0, null));
	}

	@Test
	public void testConversion_FeetToInches() {
		double result = Length.convert(1.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);
		assertEquals(12.0, result);
	}

	@Test
	public void testConversion_InchesToFeet() {
		double result = Length.convert(24.0, Length.LengthUnit.INCHES, Length.LengthUnit.FEET);
		assertEquals(2.0, result);
	}

	@Test
	public void testConversion_YardsToInches() {
		double result = Length.convert(1.0, Length.LengthUnit.YARDS, Length.LengthUnit.INCHES);
		assertEquals(36.0, result);
	}

	@Test
	public void testConversion_CentimetersToInches() {
		double result = Length.convert(2.54, Length.LengthUnit.CENTIMETERS, Length.LengthUnit.INCHES);
		assertEquals(1.0, result, 1e-6);
	}

	@Test
	public void testConversion_ZeroValue() {
		double result = Length.convert(0.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);
		assertEquals(0.0, result);
	}

	@Test
	public void testConversion_NegativeValue() {
		double result = Length.convert(-1.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);
		assertEquals(-12.0, result);
	}

	@Test
	public void testConversion_SameUnit() {
		double result = Length.convert(5.0, Length.LengthUnit.FEET, Length.LengthUnit.FEET);
		assertEquals(5.0, result);
	}

	@Test
	public void testConversion_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> Length.convert(1.0, null, Length.LengthUnit.FEET));
	}

	@Test
	public void testConversion_NaN() {
		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.NaN, Length.LengthUnit.FEET, Length.LengthUnit.INCHES));
	}

	@Test
	public void testConversion_Infinite() {
		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.POSITIVE_INFINITY, Length.LengthUnit.FEET, Length.LengthUnit.INCHES));
	}

	@Test
	public void testAddition_SameUnit_FeetPlusFeet() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(2.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(3.0, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_SameUnit_InchPlusInch() {

		Length l1 = new Length(6.0, Length.LengthUnit.INCHES);
		Length l2 = new Length(6.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(new Length(12.0, Length.LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_CrossUnit_FeetPlusInches() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(new Length(2.0, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_CrossUnit_InchPlusFeet() {

		Length l1 = new Length(12.0, Length.LengthUnit.INCHES);
		Length l2 = new Length(1.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(24.0, Length.LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_CrossUnit_YardPlusFeet() {

		Length l1 = new Length(1.0, Length.LengthUnit.YARDS);
		Length l2 = new Length(3.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(2.0, Length.LengthUnit.YARDS), result);
	}

	@Test
	public void testAddition_CrossUnit_CentimeterPlusInch() {

		Length l1 = new Length(2.54, Length.LengthUnit.CENTIMETERS);
		Length l2 = new Length(1.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertTrue(result.equals(new Length(5.08, Length.LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testAddition_Commutativity() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		assertTrue(l1.add(l2).equals(l2.add(l1)));
	}

	@Test
	public void testAddition_WithZero() {

		Length l1 = new Length(5.0, Length.LengthUnit.FEET);
		Length l2 = new Length(0.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(new Length(5.0, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_NegativeValues() {

		Length l1 = new Length(5.0, Length.LengthUnit.FEET);
		Length l2 = new Length(-2.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(3.0, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_NullSecondOperand() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> l1.add(null));
	}

	@Test
	public void testAddition_LargeValues() {

		Length l1 = new Length(1e6, Length.LengthUnit.FEET);
		Length l2 = new Length(1e6, Length.LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(2e6, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_SmallValues() {

		Length l1 = new Length(0.01, Length.LengthUnit.FEET);
		Length l2 = new Length(0.02, Length.LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(new Length(0.03, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Feet() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2, Length.LengthUnit.FEET);

		assertEquals(new Length(2.0, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Inches() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2, Length.LengthUnit.INCHES);

		assertEquals(new Length(24.0, Length.LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Yards() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2, Length.LengthUnit.YARDS);

		assertTrue(result.equals(new Length(0.6666666667, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_NullTargetUnit() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		assertThrows(IllegalArgumentException.class, () -> l1.add(l2, null));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Centimeters() {

		Length l1 = new Length(1.0, Length.LengthUnit.INCHES);
		Length l2 = new Length(1.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2, Length.LengthUnit.CENTIMETERS);

		assertTrue(result.equals(new Length(5.08, Length.LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {

		Length l1 = new Length(2.0, Length.LengthUnit.YARDS);
		Length l2 = new Length(3.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2, Length.LengthUnit.YARDS);

		assertEquals(new Length(3.0, Length.LengthUnit.YARDS), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {

		Length l1 = new Length(2.0, Length.LengthUnit.YARDS);
		Length l2 = new Length(3.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2, Length.LengthUnit.FEET);

		assertEquals(new Length(9.0, Length.LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Commutativity() {

		Length l1 = new Length(1.0, Length.LengthUnit.FEET);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		Length result1 = l1.add(l2, Length.LengthUnit.YARDS);
		Length result2 = l2.add(l1, Length.LengthUnit.YARDS);

		assertTrue(result1.equals(result2));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_WithZero() {

		Length l1 = new Length(5.0, Length.LengthUnit.FEET);
		Length l2 = new Length(0.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2, Length.LengthUnit.YARDS);

		assertTrue(result.equals(new Length(1.6666666667, Length.LengthUnit.YARDS)));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_NegativeValues() {

		Length l1 = new Length(5.0, Length.LengthUnit.FEET);
		Length l2 = new Length(-2.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2, Length.LengthUnit.INCHES);

		assertEquals(new Length(36.0, Length.LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {

		Length l1 = new Length(1000.0, Length.LengthUnit.FEET);
		Length l2 = new Length(500.0, Length.LengthUnit.FEET);

		Length result = l1.add(l2, Length.LengthUnit.INCHES);

		assertEquals(new Length(18000.0, Length.LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {

		Length l1 = new Length(12.0, Length.LengthUnit.INCHES);
		Length l2 = new Length(12.0, Length.LengthUnit.INCHES);

		Length result = l1.add(l2, Length.LengthUnit.YARDS);

		assertTrue(result.equals(new Length(0.6666666667, Length.LengthUnit.YARDS)));
	}
}