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
