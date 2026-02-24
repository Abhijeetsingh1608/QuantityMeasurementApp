# Quantity Measurement App

## **Building a Quantity Measurement System**

This document walks through the evolution of the Quantity Measurement codebase, where we progressively learned fundamental software design principles by solving increasingly complex problems. From basic equality comparisons to advanced arithmetic operations with selective support, this journey demonstrates real-world software evolution.

---

## **Final Architecture**

```
📂IMeasurable (interface)
    ├── getConversionFactor()
    ├── convertToBaseUnit()
    ├── convertFromBaseUnit()
    ├── getUnitName()
    ├── supportsArithmetic() [default: true]
    └── validateOperationSupport() [default: no-op]
        ↑
        ├──📂 LengthUnit (enum)
        │   ├── FEET
        │   ├── INCHES
        │   ├── YARDS
        │   └── CENTIMETERS
        │
        ├──📂 WeightUnit (enum)
        │   ├── KILOGRAM
        │   ├── GRAM
        │   └── POUND
        │
        ├──📂 VolumeUnit (enum)
        │   ├── LITRE
        │   ├── MILLILITRE
        │   └── GALLON
        │
        └──📂 TemperatureUnit (enum) [arithmetic disabled]
            ├── CELSIUS
            ├── FAHRENHEIT
            └── KELVIN

📂 SupportsArithmetic (functional interface)
    └── boolean isSupported()

📂 Quantity<U extends IMeasurable> (generic class)
    ├── value: double
    ├── unit: U
    ├── equals()
    ├── convertTo()
    ├── add() / add(other, targetUnit)
    ├── subtract() / subtract(other, targetUnit)
    ├── divide()
    └── ArithmeticOperation (private enum)
        ├── ADD
        ├── SUBTRACT
        └── DIVIDE

📂 QuantityMeasurementApp
    ├── demonstrateEquality<U>()
    ├── demonstrateComparison<U>()
    ├── demonstrateConversion<U>()
    ├── demonstrateAddition<U>()
    ├── demonstrateSubtraction<U>()
    ├── demonstrateDivision<U>()
    └── demonstrateTemperature()
```

---

## **Key Software Engineering Principles Learned**

| Principle | UC Stage | How Implemented |
|-----------|----------|-----------------|
| **Value Objects** | UC1 | Immutable objects representing measurements |
| **DRY** | UC3, UC10, UC13 | Generic classes and centralized operations eliminate duplication |
| **Enums as Constants** | UC3 | Type-safe unit representation |
| **Separation of Concerns** | UC8 | Units handle conversion, Quantity handles operations |
| **Single Responsibility** | UC10 | Each class has ONE clear purpose |
| **Open-Closed Principle** | UC4, UC10, UC11 | Add features without modifying existing code |
| **Liskov Substitution** | UC10, UC14 | Any IMeasurable works with Quantity (with constraints) |
| **Interface Segregation** | UC10 | Minimal, focused IMeasurable interface |
| **Dependency Inversion** | UC10 | Depend on abstraction (IMeasurable), not concrete types |
| **Generics** | UC10 | Type-safe polymorphism |
| **Composition over Inheritance** | UC3, UC10 | Quantity HAS-A unit, not IS-A specific type |
| **Strategy Pattern** | UC13 | ArithmeticOperation enum with lambda operations |
| **Template Method** | UC13, UC14 | Shared validation + operation flow with override points |
| **Functional Programming** | UC13, UC14 | Lambdas, DoubleBinaryOperator, Function<T,R> |
| **Fail-Fast Principle** | UC12, UC14 | Validate before executing operations |
| **Precision Management** | UC11 | Rounding strategies for floating-point arithmetic |
| **Selective Constraints** | UC14 | Supporting different operations for different types |

---

## Summary Timeline

<div align="center">

<table>
<tr><td align="center">

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC1-FeetEquality">
🔗 <b>UC1:</b> Basic Equality (Feet)
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC2-InchEquality">
🔗 <b>UC2:</b> Cross-Unit Comparison (Feet + Inches)
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC3-GenericLength">
🔗 <b>UC3:</b> Generic Length Class + DRY Principle
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC4-YardEquality">
🔗 <b>UC4:</b> More Units (Yards, Centimeters)
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC5-UnitConversion">
🔗 <b>UC5:</b> Unit Conversion Operations
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC5-UnitConversion">
🔗 <b>UC6:</b> Addition (Same/Different Units)
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC7-TargetUnitAddition">
🔗 <b>UC7:</b> Addition with Explicit Target Unit
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC8-StandaloneUnit">
🔗 <b>UC8:</b> Standalone Enum with Conversion Responsibility
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC9-WeightMeasurement">
🔗 <b>UC9:</b> Multi-Category Support (Weight) – Duplication Problem
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC10-MultiCategoryUnit">
🔗 <b>UC10:</b> Generic Architecture – Problem Solved
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC11-VolumeEquality">
🔗 <b>UC11:</b> Volume Measurements – Architecture Validation
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC12-QuantitySubtractionDivision">
🔗 <b>UC12:</b> Subtraction & Division – Expanding Arithmetic
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC13-CentralizedArithmeticLogic">
🔗 <b>UC13:</b> Centralized Arithmetic Logic – DRY at Operation Level
</a>

<br>⇩<br>

<a href="https://github.com/Abhijeetsingh1608/QuantityMeasurementApp/tree/feature/UC14-TemperatureManagement">
🔗 <b>UC14:</b> Temperature with Selective Arithmetic – Advanced Constraints
</a>

</td></tr>
</table>

</div>

----
