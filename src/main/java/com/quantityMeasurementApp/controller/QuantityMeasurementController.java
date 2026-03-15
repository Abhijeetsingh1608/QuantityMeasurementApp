package com.quantityMeasurementApp.controller;

import com.quantityMeasurementApp.dto.QuantityDTO;
import com.quantityMeasurementApp.exception.QuantityMeasurementException;
import com.quantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.quantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.quantityMeasurementApp.service.IQuantityMeasurementService;
import com.quantityMeasurementApp.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    public void performComparison(QuantityDTO quantity1, QuantityDTO quantity2) {
        try {
            QuantityDTO result = service.compare(quantity1, quantity2);
            System.out.println(formatComparisonResult(quantity1, quantity2, result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during comparison: " + e.getMessage());
        }
    }

    public void performConversion(QuantityDTO quantity, String targetUnit) {
        try {
            QuantityDTO result = service.convert(quantity, targetUnit);
            System.out.println(formatConversionResult(quantity, result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during conversion: " + e.getMessage());
        }
    }

    public void performAddition(QuantityDTO quantity1, QuantityDTO quantity2) {
        try {
            QuantityDTO result = service.add(quantity1, quantity2);
            System.out.println(formatArithmeticResult(quantity1, quantity2, "ADD", result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during addition: " + e.getMessage());
        }
    }

    public void performAddition(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit) {
        try {
            QuantityDTO result = service.add(quantity1, quantity2, targetUnit);
            System.out.println(formatArithmeticResult(quantity1, quantity2, "ADD", result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during addition: " + e.getMessage());
        }
    }

    public void performSubtraction(QuantityDTO quantity1, QuantityDTO quantity2) {
        try {
            QuantityDTO result = service.subtract(quantity1, quantity2);
            System.out.println(formatArithmeticResult(quantity1, quantity2, "SUBTRACT", result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during subtraction: " + e.getMessage());
        }
    }

    public void performSubtraction(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit) {
        try {
            QuantityDTO result = service.subtract(quantity1, quantity2, targetUnit);
            System.out.println(formatArithmeticResult(quantity1, quantity2, "SUBTRACT", result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during subtraction: " + e.getMessage());
        }
    }

    public void performDivision(QuantityDTO quantity1, QuantityDTO quantity2) {
        try {
            QuantityDTO result = service.divide(quantity1, quantity2);
            System.out.println(formatDivisionResult(quantity1, quantity2, result));
        } catch (QuantityMeasurementException e) {
            System.out.println("Error during division: " + e.getMessage());
        }
    }

    public QuantityDTO compare(QuantityDTO quantity1, QuantityDTO quantity2) {
        return service.compare(quantity1, quantity2);
    }

    public QuantityDTO convert(QuantityDTO quantity, String targetUnit) {
        return service.convert(quantity, targetUnit);
    }

    public QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2) {
        return service.add(quantity1, quantity2);
    }

    public QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit) {
        return service.add(quantity1, quantity2, targetUnit);
    }

    public QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2) {
        return service.subtract(quantity1, quantity2);
    }

    public QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit) {
        return service.subtract(quantity1, quantity2, targetUnit);
    }

    public QuantityDTO divide(QuantityDTO quantity1, QuantityDTO quantity2) {
        return service.divide(quantity1, quantity2);
    }

    private String formatComparisonResult(QuantityDTO q1, QuantityDTO q2, QuantityDTO result) {
        return String.format("Comparison: %.2f %s vs %.2f %s = %s",
                q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit(), result.getUnit());
    }

    private String formatConversionResult(QuantityDTO source, QuantityDTO result) {
        return String.format("Conversion: %.2f %s = %.2f %s",
                source.getValue(), source.getUnit(), result.getValue(), result.getUnit());
    }

    private String formatArithmeticResult(QuantityDTO q1, QuantityDTO q2, String operation, QuantityDTO result) {
        String op = operation.equals("ADD") ? "+" : "-";
        return String.format("%s: %.2f %s %s %.2f %s = %.2f %s",
                operation, q1.getValue(), q1.getUnit(), op, q2.getValue(), q2.getUnit(),
                result.getValue(), result.getUnit());
    }

    private String formatDivisionResult(QuantityDTO q1, QuantityDTO q2, QuantityDTO result) {
        return String.format("Division: %.2f %s / %.2f %s = %.2f",
                q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit(), result.getValue());
    }

    public static void main(String[] args) {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inches = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        controller.performComparison(feet, inches);
        controller.performAddition(feet, inches);
    }
}