package com.quantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Quantity<>(1.0, LengthUnit.YARDS).equals(new Quantity<>(1.0, LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, LengthUnit.YARDS).equals(new Quantity<>(3.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, LengthUnit.YARDS).equals(new Quantity<>(36.0, LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, LengthUnit.CENTIMETERS).equals(new Quantity<>(0.393701, LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_YardToFeet_NonEquivalentValue() {
		assertFalse(new Quantity<>(1.0, LengthUnit.YARDS).equals(new Quantity<>(2.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_CentimetersToFeet_NonEquivalentValue() {
		assertFalse(new Quantity<>(1.0, LengthUnit.CENTIMETERS).equals(new Quantity<>(1.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_MultiUnit_TransitiveProperty() {
		Quantity<LengthUnit> yard = new Quantity<>(1.0, LengthUnit.YARDS);
		Quantity<LengthUnit> feet = new Quantity<>(3.0, LengthUnit.FEET);
		Quantity<LengthUnit> inches = new Quantity<>(36.0, LengthUnit.INCHES);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yard.equals(inches));
	}

	@Test
	public void testEquality_SameReference() {
		Quantity<LengthUnit> length = new Quantity<>(2.0, LengthUnit.YARDS);
		assertTrue(length.equals(length));
	}

	@Test
	public void testEquality_NullComparison() {
		Quantity<LengthUnit> length = new Quantity<>(2.0, LengthUnit.YARDS);
		assertFalse(length.equals(null));
	}

	@Test
	public void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	public void testConversion_FeetToInches() {
		double result = Quantity.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(12.0, result);
	}

	@Test
	public void testConversion_InchesToFeet() {
		double result = Quantity.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
		assertEquals(2.0, result);
	}

	@Test
	public void testConversion_YardsToInches() {
		double result = Quantity.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
		assertEquals(36.0, result);
	}

	@Test
	public void testConversion_CentimetersToInches() {
		double result = Quantity.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
		assertEquals(1.0, result, 1e-6);
	}

	@Test
	public void testConversion_ZeroValue() {
		double result = Quantity.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(0.0, result);
	}

	@Test
	public void testConversion_NegativeValue() {
		double result = Quantity.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(-12.0, result);
	}

	@Test
	public void testConversion_SameUnit() {
		double result = Quantity.convert(5.0, LengthUnit.FEET, LengthUnit.FEET);
		assertEquals(5.0, result);
	}

	@Test
	public void testConversion_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> Quantity.convert(1.0, null, LengthUnit.FEET));
	}

	@Test
	public void testConversion_NaN() {
		assertThrows(IllegalArgumentException.class,
				() -> Quantity.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES));
	}

	@Test
	public void testConversion_Infinite() {
		assertThrows(IllegalArgumentException.class,
				() -> Quantity.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES));
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

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(2.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(3.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_SameUnit_InchPlusInch() {

		Quantity<LengthUnit> l1 = new Quantity<>(6.0, LengthUnit.INCHES);
		Quantity<LengthUnit> l2 = new Quantity<>(6.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_CrossUnit_FeetPlusInches() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_CrossUnit_InchPlusFeet() {

		Quantity<LengthUnit> l1 = new Quantity<>(12.0, LengthUnit.INCHES);
		Quantity<LengthUnit> l2 = new Quantity<>(1.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(24.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_CrossUnit_YardPlusFeet() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.YARDS);
		Quantity<LengthUnit> l2 = new Quantity<>(3.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(2.0, LengthUnit.YARDS), result);
	}

	@Test
	public void testAddition_CrossUnit_CentimeterPlusInch() {

		Quantity<LengthUnit> l1 = new Quantity<>(2.54, LengthUnit.CENTIMETERS);
		Quantity<LengthUnit> l2 = new Quantity<>(1.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2);

		assertTrue(result.equals(new Quantity<>(5.08, LengthUnit.CENTIMETERS)));
	}

	@Test
	public void testAddition_Commutativity() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		assertTrue(l1.add(l2).equals(l2.add(l1)));
	}

	@Test
	public void testAddition_WithZero() {

		Quantity<LengthUnit> l1 = new Quantity<>(5.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(0.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_NegativeValues() {

		Quantity<LengthUnit> l1 = new Quantity<>(5.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(-2.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(3.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_NullSecondOperand() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> l1.add(null));
	}

	@Test
	public void testAddition_LargeValues() {

		Quantity<LengthUnit> l1 = new Quantity<>(1e6, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(1e6, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(2e6, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_SmallValues() {

		Quantity<LengthUnit> l1 = new Quantity<>(0.01, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(0.02, LengthUnit.FEET);

		Quantity<LengthUnit> result = l1.add(l2);

		assertEquals(new Quantity<>(0.03, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Feet() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.FEET);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Inches() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.INCHES);

		assertEquals(new Quantity<>(24.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testAddition_ExplicitTargetUnit_Yards() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.YARDS);

		assertTrue(result.equals(new Quantity<>(0.67, LengthUnit.YARDS)));
	}

	@Test
	public void testAddition_ExplicitTargetUnit_NullTargetUnit() {

		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

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
		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		assertTrue(l1.equals(l2));
	}

	@Test
	public void testQuantityLengthRefactored_Add() {
		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.FEET);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testQuantityLengthRefactored_AddWithTargetUnit() {
		Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

		Quantity<LengthUnit> result = l1.add(l2, LengthUnit.YARDS);

		assertTrue(result.equals(new Quantity<>(0.67, LengthUnit.YARDS)));
	}

	@Test
	public void testQuantityLengthRefactored_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	public void testQuantityLengthRefactored_InvalidValue() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
	}

	@Test
	public void testRoundTripConversion_RefactoredDesign() {
		double original = 5.0;
		double toInches = LengthUnit.FEET.convertToBaseUnit(original);
		double backToFeet = LengthUnit.FEET.convertFromBaseUnit(toInches);

		assertEquals(original, backToFeet, 0.01);
	}

	@Test
	public void testEquality_KilogramToKilogram_SameValue() {
		assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
	}

	@Test
	public void testEquality_KilogramToKilogram_DifferentValue() {
		assertFalse(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(2.0, WeightUnit.KILOGRAM)));
	}

	@Test
	public void testEquality_GramToGram_SameValue() {
		assertTrue(new Quantity<>(100.0, WeightUnit.GRAM).equals(new Quantity<>(100.0, WeightUnit.GRAM)));
	}

	@Test
	public void testEquality_PoundToPound_SameValue() {
		assertTrue(new Quantity<>(2.0, WeightUnit.POUND).equals(new Quantity<>(2.0, WeightUnit.POUND)));
	}

	@Test
	public void testEquality_KilogramToGram_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
	}

	@Test
	public void testEquality_GramToKilogram_EquivalentValue() {
		assertTrue(new Quantity<>(1000.0, WeightUnit.GRAM).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
	}

	@Test
	public void testEquality_KilogramToPound_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(2.20462, WeightUnit.POUND)));
	}

	@Test
	public void testEquality_GramToPound_EquivalentValue() {
		assertTrue(new Quantity<>(453.592, WeightUnit.GRAM).equals(new Quantity<>(1.0, WeightUnit.POUND)));
	}

	@Test
	public void testEquality_WeightVsLength_Incompatible() {
		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
		assertFalse(weight.equals(length));
	}

	@Test
	public void testEquality_Weight_NullComparison() {
		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		assertFalse(weight.equals(null));
	}

	@Test
	public void testEquality_Weight_SameReference() {
		Quantity<WeightUnit> weight = new Quantity<>(2.0, WeightUnit.KILOGRAM);
		assertTrue(weight.equals(weight));
	}

	@Test
	public void testEquality_Weight_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	public void testEquality_Weight_TransitiveProperty() {
		Quantity<WeightUnit> kg1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> g1000 = new Quantity<>(1000.0, WeightUnit.GRAM);
		Quantity<WeightUnit> kg2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertTrue(kg1.equals(g1000));
		assertTrue(g1000.equals(kg2));
		assertTrue(kg1.equals(kg2));
	}

	@Test
	public void testEquality_Weight_ZeroValue() {
		assertTrue(new Quantity<>(0.0, WeightUnit.KILOGRAM).equals(new Quantity<>(0.0, WeightUnit.GRAM)));
	}

	@Test
	public void testEquality_Weight_NegativeWeight() {
		assertTrue(new Quantity<>(-1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(-1000.0, WeightUnit.GRAM)));
	}

	@Test
	public void testEquality_Weight_LargeValue() {
		assertTrue(new Quantity<>(1000000.0, WeightUnit.GRAM).equals(new Quantity<>(1000.0, WeightUnit.KILOGRAM)));
	}

	@Test
	public void testEquality_Weight_SmallValue() {
		assertTrue(new Quantity<>(0.001, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, WeightUnit.GRAM)));
	}

	@Test
	public void testConversion_KilogramToGram() {
		double result = Quantity.convert(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);
		assertEquals(1000.0, result, 0.01);
	}

	@Test
	public void testConversion_GramToKilogram() {
		double result = Quantity.convert(1000.0, WeightUnit.GRAM, WeightUnit.KILOGRAM);
		assertEquals(1.0, result, 0.01);
	}

	@Test
	public void testConversion_PoundToKilogram() {
		double result = Quantity.convert(2.20462, WeightUnit.POUND, WeightUnit.KILOGRAM);
		assertEquals(1.0, result, 0.01);
	}

	@Test
	public void testConversion_KilogramToPound() {
		double result = Quantity.convert(1.0, WeightUnit.KILOGRAM, WeightUnit.POUND);
		assertEquals(2.20, result, 0.01);
	}

	@Test
	public void testConversion_Weight_SameUnit() {
		Quantity<WeightUnit> result = new Quantity<>(5.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.KILOGRAM);
		assertEquals(new Quantity<>(5.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testConversion_Weight_ZeroValue() {
		Quantity<WeightUnit> result = new Quantity<>(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
		assertEquals(new Quantity<>(0.0, WeightUnit.GRAM), result);
	}

	@Test
	public void testConversion_Weight_NegativeValue() {
		Quantity<WeightUnit> result = new Quantity<>(-1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
		assertEquals(new Quantity<>(-1000.0, WeightUnit.GRAM), result);
	}

	@Test
	public void testConversion_Weight_RoundTrip() {
		Quantity<WeightUnit> original = new Quantity<>(1.5, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> converted = original.convertTo(WeightUnit.GRAM).convertTo(WeightUnit.KILOGRAM);
		assertTrue(original.equals(converted));
	}

	@Test
	public void testConversion_Weight_Infinite() {
		assertThrows(IllegalArgumentException.class,
				() -> Quantity.convert(Double.POSITIVE_INFINITY, WeightUnit.KILOGRAM, WeightUnit.GRAM));
	}

	@Test
	public void testAddition_Weight_SameUnit_KilogramPlusKilogram() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(2.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(3.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testAddition_Weight_SameUnit_GramPlusGram() {
		Quantity<WeightUnit> w1 = new Quantity<>(500.0, WeightUnit.GRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(300.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(800.0, WeightUnit.GRAM), result);
	}

	@Test
	public void testAddition_Weight_CrossUnit_KilogramPlusGram() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testAddition_Weight_CrossUnit_GramPlusKilogram() {
		Quantity<WeightUnit> w1 = new Quantity<>(500.0, WeightUnit.GRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(0.5, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(1000.0, WeightUnit.GRAM), result);
	}

	@Test
	public void testAddition_Weight_CrossUnit_PoundPlusKilogram() {
		Quantity<WeightUnit> w1 = new Quantity<>(2.20462, WeightUnit.POUND);
		Quantity<WeightUnit> w2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertTrue(result.equals(new Quantity<>(4.41, WeightUnit.POUND)));
	}

	@Test
	public void testAddition_Weight_ExplicitTargetUnit_Kilogram() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2, WeightUnit.KILOGRAM);

		assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testAddition_Weight_ExplicitTargetUnit_Gram() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2, WeightUnit.GRAM);

		assertEquals(new Quantity<>(2000.0, WeightUnit.GRAM), result);
	}

	@Test
	public void testAddition_Weight_ExplicitTargetUnit_Pound() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.POUND);
		Quantity<WeightUnit> w2 = new Quantity<>(453.59, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2, WeightUnit.POUND);

		assertTrue(result.equals(new Quantity<>(1.98, WeightUnit.POUND)));
	}

	@Test
	public void testAddition_Weight_Commutativity() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		assertTrue(w1.add(w2).equals(w2.add(w1)));
	}

	@Test
	public void testAddition_Weight_WithZero() {
		Quantity<WeightUnit> w1 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(0.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(5.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testAddition_Weight_NegativeValues() {
		Quantity<WeightUnit> w1 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(-2000.0, WeightUnit.GRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(3.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testAddition_Weight_NullSecondOperand() {
		Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertThrows(IllegalArgumentException.class, () -> w1.add(null));
	}

	@Test
	public void testAddition_Weight_LargeValues() {
		Quantity<WeightUnit> w1 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(1e6, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> result = w1.add(w2);

		assertEquals(new Quantity<>(2e6, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testWeightUnit_ConvertToBaseUnit() {
		assertEquals(1.0, WeightUnit.KILOGRAM.convertToBaseUnit(1.0), 1e-9);
		assertEquals(0.5, WeightUnit.GRAM.convertToBaseUnit(500.0), 1e-9);
		assertEquals(0.91, WeightUnit.POUND.convertToBaseUnit(2.0), 1e-2);
	}

	@Test
	public void testWeightUnit_ConvertFromBaseUnit() {
		assertEquals(1.0, WeightUnit.KILOGRAM.convertFromBaseUnit(1.0), 1e-9);
		assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), 1e-9);
		assertEquals(2.20, WeightUnit.POUND.convertFromBaseUnit(1.0), 1e-2);
	}

	@Test
	public void testIMeasurableInterface_LengthUnitImplementation() {
		assertTrue(LengthUnit.FEET instanceof IMeasurable);
		assertEquals("FEET", LengthUnit.FEET.getUnitName());
		assertEquals(12.0, LengthUnit.FEET.getConversionFactor(), 1e-9);
	}

	@Test
	public void testIMeasurableInterface_WeightUnitImplementation() {
		assertTrue(WeightUnit.KILOGRAM instanceof IMeasurable);
		assertEquals("KILOGRAM", WeightUnit.KILOGRAM.getUnitName());
		assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), 1e-9);
	}

	@Test
	public void testIMeasurableInterface_ConsistentBehavior() {
		IMeasurable lengthUnit = LengthUnit.INCHES;
		IMeasurable weightUnit = WeightUnit.GRAM;

		assertNotNull(lengthUnit.getUnitName());
		assertNotNull(weightUnit.getUnitName());
		assertTrue(lengthUnit.getConversionFactor() > 0);
		assertTrue(weightUnit.getConversionFactor() > 0);
	}

	@Test
	public void testGenericQuantity_LengthOperations_Equality() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

		assertTrue(q1.equals(q2));
	}

	@Test
	public void testGenericQuantity_WeightOperations_Equality() {
		Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		assertTrue(q1.equals(q2));
	}

	@Test
	public void testGenericQuantity_LengthOperations_Conversion() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> result = q1.convertTo(LengthUnit.INCHES);

		assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), result);
	}

	@Test
	public void testGenericQuantity_WeightOperations_Conversion() {
		Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> result = q1.convertTo(WeightUnit.GRAM);

		assertEquals(new Quantity<>(1000.0, WeightUnit.GRAM), result);
	}

	@Test
	public void testGenericQuantity_LengthOperations_Addition() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);
		Quantity<LengthUnit> result = q1.add(q2, LengthUnit.FEET);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testGenericQuantity_WeightOperations_Addition() {
		Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
		Quantity<WeightUnit> result = q1.add(q2, WeightUnit.KILOGRAM);

		assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
	}

	@Test
	public void testCrossCategoryPrevention_LengthVsWeight() {
		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertFalse(length.equals(weight));
	}

	@Test
	public void testGenericQuantity_ConstructorValidation_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	public void testGenericQuantity_ConstructorValidation_InvalidValue() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
	}

	@Test
	public void testQuantityMeasurementApp_SimplifiedDemonstration_Equality() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

		assertTrue(QuantityMeasurementApp.demonstrateEquality(q1, q2));
	}

	@Test
	public void testQuantityMeasurementApp_SimplifiedDemonstration_Conversion() {
		double result = QuantityMeasurementApp.demonstrateConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);

		assertEquals(12.0, result, 0.01);
	}

	@Test
	public void testQuantityMeasurementApp_SimplifiedDemonstration_Addition() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);
		Quantity<LengthUnit> result = QuantityMeasurementApp.demonstrateAddition(q1, q2, LengthUnit.FEET);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	}

	@Test
	public void testHashCode_GenericQuantity_Consistency() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

		assertEquals(q1.hashCode(), q2.hashCode());
	}

	@Test
	public void testEquals_GenericQuantity_ContractPreservation() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);
		Quantity<LengthUnit> q3 = new Quantity<>(1.0, LengthUnit.FEET);

		assertTrue(q1.equals(q1));
		assertTrue(q1.equals(q2));
		assertTrue(q2.equals(q1));
		assertTrue(q1.equals(q3));
		assertTrue(q3.equals(q2));
	}

	@Test
	public void testEnumAsUnitCarrier_BehaviorEncapsulation() {
		IMeasurable unit = LengthUnit.FEET;

		assertEquals(12.0, unit.convertToBaseUnit(1.0), 0.01);
		assertEquals(1.0, unit.convertFromBaseUnit(12.0), 0.01);
	}

	@Test
	public void testTypeErasure_RuntimeSafety() {
		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		Object obj1 = length;
		Object obj2 = weight;

		assertFalse(obj1.equals(obj2));
	}

	@Test
	public void testCompositionOverInheritance_Flexibility() {
		Quantity<LengthUnit> lengthQuantity = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<WeightUnit> weightQuantity = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		assertNotNull(lengthQuantity.getUnit());
		assertNotNull(weightQuantity.getUnit());
		assertEquals(LengthUnit.FEET, lengthQuantity.getUnit());
		assertEquals(WeightUnit.KILOGRAM, weightQuantity.getUnit());
		assertNotEquals(lengthQuantity.getUnit().getClass(), weightQuantity.getUnit().getClass());
	}

	@Test
	public void testImmutability_GenericQuantity() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = q1.add(new Quantity<>(1.0, LengthUnit.FEET));

		assertEquals(1.0, q1.getValue(), 1e-9);
		assertEquals(2.0, q2.getValue(), 1e-9);
	}

	private static final double EPSILON = 0.00001;

	@Test
	void testEquality_LitreToLitre_SameValue() {
		assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
	}

	@Test
	void testEquality_LitreToLitre_DifferentValue() {
		assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
	}

	@Test
	void testEquality_LitreToMillilitre_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testEquality_MillilitreToLitre_EquivalentValue() {
		assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
	}

	@Test
	void testEquality_LitreToGallon_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(0.264172, VolumeUnit.GALLON)));
	}

	@Test
	void testEquality_GallonToLitre_EquivalentValue() {
		assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON).equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
	}

	@Test
	void testEquality_VolumeVsLength_Incompatible() {
		assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, LengthUnit.FEET)));
	}

	@Test
	void testEquality_VolumeVsWeight_Incompatible() {
		assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testEquality_NullComparison1() {
		assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
	}

	@Test
	void testEquality_SameReference1() {
		Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
		assertTrue(q.equals(q));
	}

	@Test
	void testEquality_NullUnit1() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	void testEquality_TransitiveProperty() {
		Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(a.equals(b));
		assertTrue(b.equals(c));
		assertTrue(a.equals(c));
	}

	@Test
	void testEquality_ZeroValue() {
		assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE).equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testEquality_NegativeVolume() {
		assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE).equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testEquality_LargeVolumeValue() {
		assertTrue(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
	}

	@Test
	void testEquality_SmallVolumeValue() {
		assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
	}

	@Test
	void testConversion_LitreToMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_MillilitreToLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);

		assertEquals(1.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_GallonToLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE);

		assertEquals(3.78541, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_LitreToGallon() {
		Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON);

		assertEquals(1.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_RoundTrip() {
		Quantity<VolumeUnit> result = new Quantity<>(1.5, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE)
				.convertTo(VolumeUnit.LITRE);

		assertEquals(1.5, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_SameUnit_LitrePlusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(2.0, VolumeUnit.LITRE));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_CrossUnit_LitrePlusMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
				.add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

		assertEquals(2.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Gallon() {
		Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE)
				.add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON);

		assertEquals(2.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_WithZero1() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.add(new Quantity<>(0.0, VolumeUnit.MILLILITRE));

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_LitreConstant() {
		assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_MillilitreConstant() {
		assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_GallonConstant() {
		assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
	}

	@Test
	void testConvertToBaseUnit_MillilitreToLitre() {
		assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON);
	}

	@Test
	void testConvertFromBaseUnit_LitreToGallon() {
		assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
	}

}