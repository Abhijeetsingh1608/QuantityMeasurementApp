# Quantity Measurement App

## **Building a Quantity Measurement System**

This document walks through the evolution of the Quantity Measurement codebase, where we progressively learned fundamental software design principles by solving increasingly complex problems. From basic equality comparisons to advanced arithmetic operations with selective support, this journey demonstrates real-world software evolution.

---

## **UC1: Basic Feet Equality - The Foundation**

### **What we did:**
- Created a simple `Feet` class to represent measurements
- Implemented basic equality comparison: "Is 1 foot equal to 1 foot?"

### **What we learned:**
- **Value objects**: Objects that represent a concept by their value, not identity
- **Overriding equals()**: How to customize equality comparison in Java
- **Test-Driven Development (TDD)**: Writing tests first, then implementation

### **Key concept:**
```java
Feet f1 = new Feet(1.0);
Feet f2 = new Feet(1.0);
f1.equals(f2); // true - same value
```

---

## **UC2: Cross-Unit Comparison (Feet + Inches)**

### **What we did:**
- Extended equality to compare different units: "Is 1 foot equal to 12 inches?"
- Introduced conversion logic to compare apples to apples

### **What we learned:**
- **Normalization**: Converting different representations to a common base
- **Conversion factors**: Mathematical relationships between units (1 foot = 12 inches)
- **Base unit concept**: Choosing one unit as the reference (base)

### **Problem solved:**
```java
Length feet = new Length(1.0, FEET);
Length inches = new Length(12.0, INCHES);
feet.equals(inches); // true - equivalent values
```

---

## **UC3: Generic Length Class with DRY Principle**

### **What we did:**
- Replaced separate `Feet` and `Inches` classes with generic `Length` class
- Added `LengthUnit` enum to represent different units

### **What we learned:**
- **DRY (Don't Repeat Yourself)**: Eliminate code duplication
- **Enums**: Type-safe way to represent fixed sets of constants
- **Composition**: Combining value + unit instead of separate classes
- **Single class, multiple units**: More scalable than one class per unit

### **Design evolution:**
```
Before: Feet class, Inches class, Yards class...
After:  Length class + LengthUnit enum
```

---

## **UC4: Adding More Units (Yards + Centimeters)**

### **What we did:**
- Added YARDS and CENTIMETERS to the `LengthUnit` enum
- Made sure all units work seamlessly with existing code

### **What we learned:**
- **Open-Closed Principle (OCP)**: Open for extension, closed for modification
- **Scalability**: Adding units is now easy - just add enum constants
- **Consistency**: All units follow the same pattern

### **Scalability demonstration:**
```java
// Adding a new unit is just one line!
enum LengthUnit {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701) // New unit added easily
}
```

---

## **UC5: Unit Conversion Operations**

### **What we did:**
- Added `convert()` and `convertTo()` methods
- Implemented actual unit conversion, not just comparison

### **What we learned:**
- **Static utility methods**: `Length.convert(value, from, to)` for conversions
- **Immutability**: Operations return new objects instead of modifying existing ones
- **Precision handling**: Rounding to 2 decimal places to manage floating-point errors

### **Usage:**
```java
Length feet = new Length(1.0, FEET);
Length inches = feet.convertTo(INCHES); // Returns new Length(12.0, INCHES)
```

---

## **UC6: Addition - Same and Different Units**

### **What we did:**
- Implemented addition of quantities in same or different units
- Result inherits the unit of the first operand

### **What we learned:**
- **Method overloading**: Multiple versions of `add()` method
- **Unit normalization**: Convert to base unit, add, convert back
- **Operator design**: Choosing sensible defaults (result in first operand's unit)

### **Example:**
```java
Length l1 = new Length(1.0, FEET);
Length l2 = new Length(12.0, INCHES);
Length result = l1.add(l2); // Returns 2.0 FEET
```

---
