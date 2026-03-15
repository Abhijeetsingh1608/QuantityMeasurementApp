package com.quantityMeasurementApp;

import com.quantityMeasurementApp.controller.QuantityMeasurementController;
import com.quantityMeasurementApp.dto.QuantityDTO;
import com.quantityMeasurementApp.model.QuantityMeasurementEntity;
import com.quantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.quantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.quantityMeasurementApp.service.IQuantityMeasurementService;
import com.quantityMeasurementApp.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {

    private static QuantityMeasurementApp instance;
    private final QuantityMeasurementController controller;
    private final IQuantityMeasurementRepository repository;

    private QuantityMeasurementApp() {
        this.repository = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        this.controller = new QuantityMeasurementController(service);
    }

    public static QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    public QuantityMeasurementController getController() {
        return controller;
    }

    public IQuantityMeasurementRepository getRepository() {
        return repository;
    }

    public static QuantityMeasurementController createController() {
        return getInstance().getController();
    }

    public static IQuantityMeasurementService createService(IQuantityMeasurementRepository repository) {
        return new QuantityMeasurementServiceImpl(repository);
    }

    public static IQuantityMeasurementRepository createRepository() {
        return QuantityMeasurementCacheRepository.getInstance();
    }

    private void demonstrateLengthOperations() {
        System.out.println("\n=== Length Operations ===");

        QuantityDTO feet1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inches12 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO yards1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARDS);
        QuantityDTO feet3 = new QuantityDTO(3.0, QuantityDTO.LengthUnit.FEET);

        controller.performComparison(feet1, inches12);
        controller.performComparison(yards1, feet3);

        controller.performConversion(feet1, "INCHES");
        controller.performConversion(yards1, "FEET");

        controller.performAddition(feet1, inches12);
        controller.performAddition(feet1, inches12, "YARDS");

        QuantityDTO feet5 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO feet2 = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        controller.performSubtraction(feet5, feet2);

        QuantityDTO feet6 = new QuantityDTO(6.0, QuantityDTO.LengthUnit.FEET);
        controller.performDivision(feet6, feet2);
    }

    private void demonstrateWeightOperations() {
        System.out.println("\n=== Weight Operations ===");

        QuantityDTO kg1 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO grams1000 = new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM);
        QuantityDTO pound1 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.POUND);

        controller.performComparison(kg1, grams1000);

        controller.performConversion(kg1, "GRAM");
        controller.performConversion(pound1, "KILOGRAM");

        controller.performAddition(kg1, grams1000);
        controller.performAddition(kg1, grams1000, "GRAM");
    }

    private void demonstrateVolumeOperations() {
        System.out.println("\n=== Volume Operations ===");

        QuantityDTO litre1 = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO ml1000 = new QuantityDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE);
        QuantityDTO gallon1 = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.GALLON);

        controller.performComparison(litre1, ml1000);

        controller.performConversion(litre1, "MILLILITRE");
        controller.performConversion(gallon1, "LITRE");

        controller.performAddition(litre1, ml1000);

        QuantityDTO litre5 = new QuantityDTO(5.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO ml500 = new QuantityDTO(500.0, QuantityDTO.VolumeUnit.MILLILITRE);
        controller.performSubtraction(litre5, ml500);
        controller.performDivision(litre5, ml500);
    }

    private void demonstrateTemperatureOperations() {
        System.out.println("\n=== Temperature Operations ===");

        QuantityDTO celsius0 = new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO fahrenheit32 = new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
        QuantityDTO kelvin273 = new QuantityDTO(273.15, QuantityDTO.TemperatureUnit.KELVIN);

        controller.performComparison(celsius0, fahrenheit32);
        controller.performComparison(celsius0, kelvin273);

        controller.performConversion(celsius0, "FAHRENHEIT");
        controller.performConversion(celsius0, "KELVIN");

        System.out.println("\nAttempting temperature addition (should fail):");
        QuantityDTO celsius10 = new QuantityDTO(10.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO celsius20 = new QuantityDTO(20.0, QuantityDTO.TemperatureUnit.CELSIUS);
        controller.performAddition(celsius10, celsius20);
    }

    private void demonstrateCrossCategoryPrevention() {
        System.out.println("\n=== Cross-Category Operation Prevention ===");

        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");

        System.out.println("Attempting to compare length with weight (should fail):");
        controller.performComparison(feet, kg);

        System.out.println("\nAttempting to add length with weight (should fail):");
        controller.performAddition(feet, kg);
    }

    private void displayStoredMeasurements() {
        System.out.println("\n=== Stored Measurements ===");
        var measurements = repository.getAllMeasurements();
        System.out.println("Total measurements stored: " + measurements.size());
        for (QuantityMeasurementEntity entity : measurements) {
            System.out.println("  " + entity);
        }
    }

    public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {
        return q1.equals(q2);
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2) {
        Quantity<U> result = q1.subtract(q2);
        System.out.println("Subtraction Result: " + result);
        return result;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> result = q1.subtract(q2, targetUnit);
        System.out.println("Subtraction Result: " + result);
        return result;
    }

    public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
        double result = q1.divide(q2);
        System.out.println("Division Result: " + result);
        return result;
    }

    public static <U extends IMeasurable> boolean demonstrateComparison(double value1, U unit1, double value2, U unit2) {
        Quantity<U> q1 = new Quantity<>(value1, unit1);
        Quantity<U> q2 = new Quantity<>(value2, unit2);
        boolean result = q1.equals(q2);
        System.out.println("quantities are equal : " + result);
        return result;
    }

    public static <U extends IMeasurable> double demonstrateConversion(double value, U from, U to) {
        double result = Quantity.convert(value, from, to);
        System.out.println(value + " " + from.getUnitName() + " = " + result + " " + to.getUnitName());
        return result;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2) {
        Quantity<U> result = q1.add(q2);
        System.out.println("Addition : " + result);
        return result;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> result = q1.add(q2, targetUnit);
        System.out.println("Addition : " + result);
        return result;
    }

    public static <U extends IMeasurable> void demonstrateConversion(Quantity<U> quantity, U targetUnit) {
        Quantity<U> converted = quantity.convertTo(targetUnit);
        System.out.println("Original: " + quantity);
        System.out.println("Converted: " + converted);
    }

    public static void main(String[] args) {

        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();

        app.demonstrateLengthOperations();
        app.demonstrateWeightOperations();
        app.demonstrateVolumeOperations();
        app.demonstrateTemperatureOperations();
        app.demonstrateCrossCategoryPrevention();
        app.displayStoredMeasurements();

        System.out.println("\n=== Legacy Operations (Backward Compatibility) ===");

        demonstrateComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
        demonstrateConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES));

        Quantity<VolumeUnit> v1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);
        demonstrateSubtraction(v1, v2);
        demonstrateDivision(v1, v2);
    }
}