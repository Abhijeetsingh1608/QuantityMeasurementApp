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

## **UC7: Addition with Explicit Target Unit**

### **What we did:**
- Added `add(other, targetUnit)` method
- User specifies desired result unit

### **What we learned:**
- **API flexibility**: Giving users control over output format
- **Method overloading patterns**: Convenience method + explicit method
- **Default parameters**: Using overloading to simulate default arguments

### **User control:**
```java
l1.add(l2, YARDS);  // Result in yards
l1.add(l2, INCHES); // Result in inches
```

---

## **UC8: Standalone Enum with Conversion Responsibility**

### **What we did:**
- Extracted `LengthUnit` from nested enum to standalone enum
- Moved conversion logic INTO the enum itself
- Changed base unit from feet to inches

### **What we learned:**
- **Separation of Concerns**: Enum handles conversions, class handles operations
- **Delegation**: `Length` delegates to `LengthUnit` for conversions
- **Enum as behavior carrier**: Enums can have methods, not just constants
- **Refactoring without breaking**: All tests still pass after refactoring

### **Architecture improvement:**
```java
// Enum now has intelligence
public enum LengthUnit {
    FEET(12.0);
    
    private final double conversionFactor;
    
    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
    
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }
    
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}
```

---

## **UC9: Multi-Category Support (Weight Measurements)**

### **What we did:**
- Created `WeightUnit` enum (KILOGRAM, GRAM, POUND)
- Created `Weight` class mirroring `Length` pattern
- Added demonstration methods for weight operations

### **What we learned:**
- **Pattern replication**: Following established patterns for new features
- **Category separation**: Weight and Length are incompatible (can't compare)
- **Type safety**: instanceof checks prevent cross-category comparisons

### **Problem introduced:**
```
Code duplication: Length and Weight classes are nearly identical
WeightUnit and LengthUnit have duplicate structure
QuantityMeasurementApp has duplicate methods
Not scalable: Adding Volume or Temperature means more duplication
```

---

## **UC10: Generic Architecture - The Breakthrough**

### **What we did:**
- Created `IMeasurable` interface - contract for all unit types
- Created generic `Quantity<U extends IMeasurable>` class
- Made both `LengthUnit` and `WeightUnit` implement `IMeasurable`
- Simplified `QuantityMeasurementApp` to use generic methods

### **What we learned:**

#### 1. Generic Programming
```java
Quantity<LengthUnit> length = new Quantity<>(1.0, FEET);
Quantity<WeightUnit> weight = new Quantity<>(1.0, KILOGRAM);
```
- One class works with ANY unit type
- Compile-time type safety
- No code duplication

#### 2. Interface-Based Design
```java
public interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}
```
- Defines a contract all units must follow
- Enables polymorphism
- Allows treating different unit types uniformly

#### 3. Bounded Type Parameters
```java
public class Quantity<U extends IMeasurable>
```
- `U` can be ANY type that implements `IMeasurable`
- Compiler enforces this constraint
- Type safety without sacrificing flexibility

#### 4. DRY Principle Mastered
- **Before UC10**: Duplicate Length and Weight classes
- **After UC10**: Single Quantity class for ALL categories
- **Impact**: ~200 lines of code eliminated

#### 5. Single Responsibility Principle
```java
// Before: 10 methods (5 for length, 5 for weight)
demonstrateLengthEquality()
demonstrateWeightEquality()
demonstrateLengthConversion()
demonstrateWeightConversion()
...

// After: 5 generic methods
demonstrateEquality<U>()     // Works for ALL types
demonstrateConversion<U>()   // Works for ALL types
...
```

#### 6. Open-Closed Principle
- System is **OPEN** for adding new categories
- System is **CLOSED** for modification

**Adding a new category (e.g., Volume):**
```java
// 1. Create enum implementing IMeasurable
public enum VolumeUnit implements IMeasurable {
    LITER(1.0),
    MILLILITER(0.001),
    GALLON(3.78541);
    // ... implement interface methods
}

// 2. Use it immediately - NO OTHER CHANGES NEEDED!
Quantity<VolumeUnit> vol = new Quantity<>(1.0, LITER);
vol.equals(new Quantity<>(1000.0, MILLILITER)); // Works!
```

#### 7. Liskov Substitution Principle
- Any `IMeasurable` implementation can be used with `Quantity<U>`
- No special cases needed
- Substitutable without breaking functionality

#### 8. Type Erasure Handling
```java
// Generic type info erased at runtime, so we check manually
if (this.unit.getClass() != that.unit.getClass())
    return false; // Prevents comparing length to weight
```

#### 9. Polymorphism
```java
IMeasurable unit = LengthUnit.FEET;  // Polymorphic reference
unit.convertToBaseUnit(1.0);          // Works!

unit = WeightUnit.KILOGRAM;           // Different type
unit.convertToBaseUnit(1.0);          // Still works!
```

---

## **UC11: Volume Measurements - Testing Generic Architecture**

### **What we did:**
- Created `VolumeUnit` enum (LITRE, MILLILITRE, GALLON)
- Implemented volume-to-volume conversions
- Added volume addition operations
- Applied precision rounding (2 decimal places)

### **What we learned:**
- **Architecture validation**: UC10's generic design works perfectly for new categories
- **Precision management**: Floating-point arithmetic requires rounding strategies
- **Zero-modification extension**: Added entire category without changing existing code

### **Key implementation:**
```java
public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);
    
    // Rounding prevents floating-point errors
    @Override
    public double convertToBaseUnit(double value) {
        double result = value * conversionFactor;
        return Math.round(result * 100.0) / 100.0;
    }
}
```

### **Problem solved:**
```java
Quantity<VolumeUnit> gallon = new Quantity<>(1.0, GALLON);
Quantity<VolumeUnit> litre = new Quantity<>(3.78, LITRE);
gallon.equals(litre); // true - equivalent after rounding

Quantity<VolumeUnit> sum = gallon.add(litre); // 2.0 GALLON
```

---

## **UC12: Subtraction and Division - Expanding Arithmetic Operations**

### **What we did:**
- Implemented `subtract()` method with same/explicit target unit
- Implemented `divide()` method returning ratio
- Added comprehensive validation for all arithmetic operations
- Centralized validation logic to avoid duplication

### **What we learned:**
- **Consistent API design**: Subtraction mirrors addition's dual-method pattern
- **Division semantics**: Returns scalar (double), not Quantity
- **Validation patterns**: Consistent error handling across operations
- **Edge case handling**: Division by zero protection with epsilon comparison

### **Key implementations:**

#### Subtraction:
```java
Quantity<LengthUnit> l1 = new Quantity<>(5.0, FEET);
Quantity<LengthUnit> l2 = new Quantity<>(3.0, FEET);
Quantity<LengthUnit> diff = l1.subtract(l2); // 2.0 FEET

// With explicit target unit
Quantity<LengthUnit> diffInches = l1.subtract(l2, INCHES); // 24.0 INCHES
```

#### Division:
```java
Quantity<LengthUnit> l1 = new Quantity<>(6.0, FEET);
Quantity<LengthUnit> l2 = new Quantity<>(3.0, FEET);
double ratio = l1.divide(l2); // 2.0 (dimensionless)
```

### **Validation strategy:**
```java
private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {
    // Null check
    // Category compatibility check
    // Finite value check
    // Target unit check (conditional)
}
```

---

## **UC13: Centralized Arithmetic Logic - DRY at Operation Level**

### **What we did:**
- Created `ArithmeticOperation` enum encapsulating operation logic
- Refactored `add()`, `subtract()`, `divide()` to use centralized helpers
- Eliminated code duplication across arithmetic methods
- Introduced functional programming with `DoubleBinaryOperator`

### **What we learned:**
- **Strategy Pattern**: Enum-based strategy for different operations
- **Lambda expressions**: Using lambdas for operation logic
- **Template Method Pattern**: Shared validation + operation + conversion flow
- **DRY at algorithm level**: Don't repeat the "convert → operate → convert back" pattern
- **Code reduction**: 50% fewer lines of code with improved maintainability

### **Architecture breakthrough:**

#### ArithmeticOperation Enum:
```java
private enum ArithmeticOperation {
    ADD((a, b) -> a + b),
    SUBTRACT((a, b) -> a - b),
    DIVIDE((a, b) -> {
        if (Math.abs(b) < EPSILON) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    });
    
    private final DoubleBinaryOperator operation;
    
    ArithmeticOperation(DoubleBinaryOperator operation) {
        this.operation = operation;
    }
    
    public double compute(double a, double b) {
        return operation.applyAsDouble(a, b);
    }
}
```

#### Centralized Helper:
```java
private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
    double thisBase = unit.convertToBaseUnit(this.value);
    double otherBase = other.unit.convertToBaseUnit(other.value);
    return operation.compute(thisBase, otherBase);
}

// Usage:
double result = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
```

### **Benefits achieved:**
1. **Single source of truth**: Operation logic defined once
2. **Easy extension**: Adding multiply/modulo means adding one enum constant
3. **Consistent behavior**: All operations use same validation flow
4. **Testability**: Can test operations independently
5. **Functional programming**: Leveraging Java's functional capabilities

---
