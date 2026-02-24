package com.quantityMeasurementApp;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

		assertEquals(3.79, result.getValue(), EPSILON);
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

	// =====================
	// SUBTRACTION TESTS
	// =====================

	@Test
	void testSubtraction_SameUnit_FeetMinusFeet() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(5.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	void testSubtraction_SameUnit_LitreMinusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(10.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(3.0, VolumeUnit.LITRE));

		assertEquals(7.0, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_CrossUnit_FeetMinusInches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCHES));

		assertEquals(9.5, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Inches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);

		assertEquals(114.0, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_ResultingInNegative() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(-5.0, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_ResultingInZero() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(120.0, LengthUnit.INCHES));

		assertEquals(0.0, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_WithZeroOperand() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(0.0, LengthUnit.INCHES));

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_WithNegativeValues() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(-2.0, LengthUnit.FEET));

		assertEquals(7.0, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_NonCommutative() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		assertEquals(5.0, a.subtract(b).getValue(), EPSILON);
		assertEquals(-5.0, b.subtract(a).getValue(), EPSILON);
	}

	@Test
	void testSubtraction_NullOperand() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
	}

	@Test
	void testSubtraction_NullTargetUnit() {
		assertThrows(IllegalArgumentException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET), null));
	}

//	@Test
//	void testSubtraction_CrossCategory() {
//		assertThrows(IllegalArgumentException.class,
//				() -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM)));
//	}

	@Test
	void testSubtraction_ChainedOperations() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(2.0, LengthUnit.FEET)).subtract(new Quantity<>(1.0, LengthUnit.FEET));

		assertEquals(7.0, result.getValue(), EPSILON);
	}

	@Test
	void testDivision_SameUnit_FeetDividedByFeet() {
		double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(5.0, result, EPSILON);
	}

	@Test
	void testDivision_CrossUnit_FeetDividedByInches() {
		double result = new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(1.0, result, EPSILON);
	}

	@Test
	void testDivision_RatioGreaterThanOne() {
		assertEquals(5.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
	}

	@Test
	void testDivision_RatioLessThanOne() {
		assertEquals(0.5, new Quantity<>(5.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET)), EPSILON);
	}

	@Test
	void testDivision_RatioEqualToOne() {
		assertEquals(1.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET)), EPSILON);
	}

	@Test
	void testDivision_NonCommutative() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		assertEquals(2.0, a.divide(b), EPSILON);
		assertEquals(0.5, b.divide(a), EPSILON);
	}

	@Test
	void testDivision_ByZero() {
		assertThrows(ArithmeticException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
	}

	@Test
	void testDivision_WithLargeRatio() {
		assertEquals(1e6, new Quantity<>(1e6, WeightUnit.KILOGRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)),
				EPSILON);
	}

	@Test
	void testDivision_WithSmallRatio() {
		assertEquals(1e-6, new Quantity<>(1.0, WeightUnit.KILOGRAM).divide(new Quantity<>(1e6, WeightUnit.KILOGRAM)),
				EPSILON);
	}

	@Test
	void testDivision_NullOperand() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
	}

//	@Test
//	void testDivision_CrossCategory() {
//		assertThrows(IllegalArgumentException.class,
//				() -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM)));
//	}

	@Test
	void testSubtractionAddition_Inverse() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		Quantity<LengthUnit> result = a.add(b).subtract(b);

		assertEquals(a.getValue(), result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_Immutability() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		a.subtract(b);

		assertEquals(10.0, a.getValue(), EPSILON);
		assertEquals(5.0, b.getValue(), EPSILON);
	}

	@Test
	void testDivision_Immutability() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		a.divide(b);

		assertEquals(10.0, a.getValue(), EPSILON);
		assertEquals(5.0, b.getValue(), EPSILON);
	}

	@Test
	void testValidation_NullOperand_ConsistentAcrossOperations() {
		Quantity<LengthUnit> q = new Quantity<>(10, LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> q.add(null));
		assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
		assertThrows(IllegalArgumentException.class, () -> q.divide(null));
	}

	@Test
	void testValidation_CrossCategory_ConsistentAcrossOperations() {
		Quantity<LengthUnit> length = new Quantity<>(10, LengthUnit.FEET);
		Quantity<WeightUnit> weight = new Quantity<>(5, WeightUnit.KILOGRAM);

		assertThrows(IllegalArgumentException.class, () -> length.add((Quantity) weight));
		assertThrows(IllegalArgumentException.class, () -> length.subtract((Quantity) weight));
		assertThrows(IllegalArgumentException.class, () -> length.divide((Quantity) weight));
	}

	@Test
	void testValidation_FiniteValue_ConsistentAcrossOperations() {
		Quantity<LengthUnit> valid = new Quantity<>(10, LengthUnit.FEET);

		// Test that constructor rejects infinite values
		assertThrows(IllegalArgumentException.class, 
				() -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
		assertThrows(IllegalArgumentException.class, 
				() -> new Quantity<>(Double.NEGATIVE_INFINITY, LengthUnit.FEET));
		assertThrows(IllegalArgumentException.class, 
				() -> new Quantity<>(Double.NaN, LengthUnit.FEET));
	}

	@Test
	void testArithmeticOperation_Add_EnumComputation() throws Exception {
		Class<?> enumClass = Class.forName("com.quantityMeasurementApp.Quantity$ArithmeticOperation");
		Object add = Enum.valueOf((Class<Enum>) enumClass, "ADD");

		Method compute = enumClass.getDeclaredMethod("compute", double.class, double.class);
		compute.setAccessible(true);

		double result = (double) compute.invoke(add, 10.0, 5.0);
		assertEquals(15.0, result, EPSILON);
	}

	@Test
	void testArithmeticOperation_Subtract_EnumComputation() throws Exception {
		Class<?> enumClass = Class.forName("com.quantityMeasurementApp.Quantity$ArithmeticOperation");
		Object sub = Enum.valueOf((Class<Enum>) enumClass, "SUBTRACT");

		Method compute = enumClass.getDeclaredMethod("compute", double.class, double.class);
		compute.setAccessible(true);

		double result = (double) compute.invoke(sub, 10.0, 5.0);
		assertEquals(5.0, result, EPSILON);
	}

	@Test
	void testArithmeticOperation_Divide_EnumComputation() throws Exception {
		Class<?> enumClass = Class.forName("com.quantityMeasurementApp.Quantity$ArithmeticOperation");
		Object div = Enum.valueOf((Class<Enum>) enumClass, "DIVIDE");

		Method compute = enumClass.getDeclaredMethod("compute", double.class, double.class);
		compute.setAccessible(true);

		double result = (double) compute.invoke(div, 10.0, 5.0);
		assertEquals(2.0, result, EPSILON);
	}

	@Test
	void testAdd_UC12_BehaviorPreserved() {
		Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

		Quantity<LengthUnit> result = q1.add(q2);

		assertEquals(2.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	void testSubtract_UC12_BehaviorPreserved() {
		Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCHES);

		Quantity<LengthUnit> result = q1.subtract(q2);

		assertEquals(9.5, result.getValue(), EPSILON);
	}

	@Test
	void testDivide_UC12_BehaviorPreserved() {
		Quantity<LengthUnit> q1 = new Quantity<>(24, LengthUnit.INCHES);
		Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

		double result = q1.divide(q2);

		assertEquals(1.0, result, EPSILON);
	}

	@Test
	void testRounding_AddSubtract_TwoDecimalPlaces() {
		Quantity<LengthUnit> q1 = new Quantity<>(1.2345, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(1.1111, LengthUnit.FEET);

		Quantity<LengthUnit> result = q1.add(q2);

		assertEquals(2.35, result.getValue(), 0.01);
	}

	@Test
	void testRounding_Divide_NoRounding() {
		Quantity<LengthUnit> q1 = new Quantity<>(7, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

		double result = q1.divide(q2);

		assertEquals(3.5, result, EPSILON);
	}

	@Test
	void testImmutability_AfterAdd() {
		Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

		q1.add(q2);

		assertEquals(10, q1.getValue());
		assertEquals(5, q2.getValue());
	}

	@Test
	void testImmutability_AfterSubtract() {
		Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

		q1.subtract(q2);

		assertEquals(10, q1.getValue());
		assertEquals(5, q2.getValue());
	}

	@Test
	void testImmutability_AfterDivide() {
		Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

		q1.divide(q2);

		assertEquals(10, q1.getValue());
		assertEquals(5, q2.getValue());
	}

	@Test
	void testAllOperations_AcrossAllCategories() {

		// Length
		Quantity<LengthUnit> l1 = new Quantity<>(10, LengthUnit.FEET);
		Quantity<LengthUnit> l2 = new Quantity<>(5, LengthUnit.FEET);
		assertEquals(5, l1.subtract(l2).getValue());

		// Weight
		Quantity<WeightUnit> w1 = new Quantity<>(10, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> w2 = new Quantity<>(5, WeightUnit.KILOGRAM);
		assertEquals(2, w1.divide(w2));

		// Volume
		Quantity<VolumeUnit> v1 = new Quantity<>(5, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(2, VolumeUnit.LITRE);
		assertEquals(3, v1.subtract(v2).getValue());
	}

	@Test
	void testHelper_PrivateVisibility() throws Exception {
		Method method = Quantity.class.getDeclaredMethod("performBaseArithmetic", Quantity.class,
				Class.forName("com.quantityMeasurementApp.Quantity$ArithmeticOperation"));

		assertTrue(Modifier.isPrivate(method.getModifiers()));
	}

	@Test
	void testTemperatureEquality_CelsiusToCelsius_SameValue() {
		assertTrue(new Quantity<>(0.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(0.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
		assertTrue(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)
				.equals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)));
	}

	@Test
	void testTemperatureEquality_KelvinToKelvin_SameValue() {
		assertTrue(
				new Quantity<>(273.15, TemperatureUnit.KELVIN).equals(new Quantity<>(273.15, TemperatureUnit.KELVIN)));
	}

	@Test
	void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit() {
		assertTrue(
				new Quantity<>(0.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)));
	}

	@Test
	void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal() {
		assertTrue(new Quantity<>(-40.0, TemperatureUnit.CELSIUS)
				.equals(new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT)));
	}

	@Test
	void testTemperatureEquality_SymmetricProperty() {
		Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
		Quantity<TemperatureUnit> b = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}

	@Test
	void testTemperatureEquality_ReflexiveProperty() {
		Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
		assertTrue(t.equals(t));
	}

	@Test
	void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {
		Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);

		Quantity<TemperatureUnit> result = t.convertTo(TemperatureUnit.FAHRENHEIT);

		assertEquals(212.0, result.getValue(), EPSILON);
	}

	@Test
	void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {
		Quantity<TemperatureUnit> t = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

		Quantity<TemperatureUnit> result = t.convertTo(TemperatureUnit.CELSIUS);

		assertEquals(0.0, result.getValue(), EPSILON);
	}

	@Test
	void testTemperatureConversion_CelsiusToKelvin() {
		Quantity<TemperatureUnit> t = new Quantity<>(0.0, TemperatureUnit.CELSIUS);

		Quantity<TemperatureUnit> result = t.convertTo(TemperatureUnit.KELVIN);

		assertEquals(273.15, result.getValue(), EPSILON);
	}

	@Test
	void testTemperatureConversion_AbsoluteZero() {
		Quantity<TemperatureUnit> c = new Quantity<>(-273.15, TemperatureUnit.CELSIUS);

		Quantity<TemperatureUnit> k = c.convertTo(TemperatureUnit.KELVIN);

		assertEquals(0.0, k.getValue(), EPSILON);
	}

	@Test
	void testTemperatureConversion_RoundTrip_PreservesValue() {
		Quantity<TemperatureUnit> original = new Quantity<>(50.0, TemperatureUnit.CELSIUS);

		Quantity<TemperatureUnit> converted = original.convertTo(TemperatureUnit.FAHRENHEIT)
				.convertTo(TemperatureUnit.CELSIUS);

		assertEquals(50.0, converted.getValue(), EPSILON);
	}

	@Test
	void testTemperatureUnsupportedOperation_Add() {
		Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);

		assertThrows(UnsupportedOperationException.class, () -> t.add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureUnsupportedOperation_Subtract() {
		Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);

		assertThrows(UnsupportedOperationException.class,
				() -> t.subtract(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureUnsupportedOperation_Divide() {
		Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);

		assertThrows(UnsupportedOperationException.class,
				() -> t.divide(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureUnsupportedOperation_ErrorMessage() {
		Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);

		UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
				() -> t.add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));

		assertTrue(ex.getMessage().contains("Temperature"));
	}

	@Test
	void testTemperatureVsLengthIncompatibility() {
		assertFalse(new Quantity<>(100.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(100.0, LengthUnit.FEET)));
	}

	@Test
	void testTemperatureVsWeightIncompatibility() {
		assertFalse(new Quantity<>(50.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(50.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testTemperatureVsVolumeIncompatibility() {
		assertFalse(new Quantity<>(25.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(25.0, VolumeUnit.LITRE)));
	}

	@Test
	void testOperationSupportMethods_TemperatureUnitAddition() {
		assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
	}

	@Test
	void testOperationSupportMethods_LengthUnitAddition() {
		assertTrue(LengthUnit.FEET.supportsArithmetic());
	}

	@Test
	void testOperationSupportMethods_WeightUnitDivision() {
		assertTrue(WeightUnit.KILOGRAM.supportsArithmetic());
	}

	@Test
	void testTemperatureNullUnitValidation() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(100.0, null));
	}

	@Test
	void testTemperatureDifferentValuesInequality() {
		assertFalse(
				new Quantity<>(50.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(100.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureEnumImplementsIMeasurable() {
		assertTrue(IMeasurable.class.isAssignableFrom(TemperatureUnit.class));
	}

	@Test
	void testTemperatureIntegrationWithGenericQuantity() {
		Quantity<TemperatureUnit> t = new Quantity<>(0.0, TemperatureUnit.CELSIUS);

		assertNotNull(t);
	}
}