package com.quantityMeasurementApp.service;

import com.quantityMeasurementApp.IMeasurable;
import com.quantityMeasurementApp.Quantity;
import com.quantityMeasurementApp.dto.QuantityDTO;
import com.quantityMeasurementApp.exception.QuantityMeasurementException;
import com.quantityMeasurementApp.model.QuantityMeasurementEntity;
import com.quantityMeasurementApp.model.QuantityModel;
import com.quantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.quantityMeasurementApp.repository.QuantityMeasurementCacheRepository;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityDTO compare(QuantityDTO quantity1, QuantityDTO quantity2) {
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());

            QuantityModel<IMeasurable> model1 = new QuantityModel<>(quantity1.getValue(), unit1);
            QuantityModel<IMeasurable> model2 = new QuantityModel<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            boolean isEqual = q1.equals(q2);
            String result = isEqual ? "Equal" : "Not Equal";

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "COMPARE", result);
            repository.save(entity);

            return new QuantityDTO(isEqual ? 1.0 : 0.0, result, "Comparison");

        } catch (Exception e) {
            handleException(quantity1, quantity2, "COMPARE", e);
            throw new QuantityMeasurementException("Comparison failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO quantity, String targetUnit) {
        try {
            if (quantity == null) {
                throw new IllegalArgumentException("Quantity cannot be null");
            }

            IMeasurable sourceUnit = IMeasurable.getUnitByName(quantity.getUnit(), quantity.getMeasurementType());
            IMeasurable targetUnitObj = IMeasurable.getUnitByName(targetUnit, quantity.getMeasurementType());

            QuantityModel<IMeasurable> sourceModel = new QuantityModel<>(quantity.getValue(), sourceUnit);

            double convertedValue = Quantity.convert(quantity.getValue(), sourceUnit, targetUnitObj);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(convertedValue, targetUnitObj);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(sourceModel, null, "CONVERT", resultModel);
            repository.save(entity);

            return new QuantityDTO(convertedValue, targetUnit, quantity.getMeasurementType());

        } catch (Exception e) {
            handleException(quantity, null, "CONVERT", e);
            throw new QuantityMeasurementException("Conversion failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2) {
        return add(quantity1, quantity2, quantity1.getUnit());
    }

    @Override
    public QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit) {
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());
            IMeasurable targetUnitObj = IMeasurable.getUnitByName(targetUnit, quantity1.getMeasurementType());

            QuantityModel<IMeasurable> model1 = new QuantityModel<>(quantity1.getValue(), unit1);
            QuantityModel<IMeasurable> model2 = new QuantityModel<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> result = q1.add(q2, targetUnitObj);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result.getValue(), result.getUnit());

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "ADD", resultModel);
            repository.save(entity);

            return new QuantityDTO(result.getValue(), targetUnit, quantity1.getMeasurementType());

        } catch (UnsupportedOperationException e) {
            handleException(quantity1, quantity2, "ADD", e);
            throw new QuantityMeasurementException("Addition not supported: " + e.getMessage(), e);
        } catch (Exception e) {
            handleException(quantity1, quantity2, "ADD", e);
            throw new QuantityMeasurementException("Addition failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2) {
        return subtract(quantity1, quantity2, quantity1.getUnit());
    }

    @Override
    public QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit) {
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());
            IMeasurable targetUnitObj = IMeasurable.getUnitByName(targetUnit, quantity1.getMeasurementType());

            QuantityModel<IMeasurable> model1 = new QuantityModel<>(quantity1.getValue(), unit1);
            QuantityModel<IMeasurable> model2 = new QuantityModel<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> result = q1.subtract(q2, targetUnitObj);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result.getValue(), result.getUnit());

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "SUBTRACT", resultModel);
            repository.save(entity);

            return new QuantityDTO(result.getValue(), targetUnit, quantity1.getMeasurementType());

        } catch (UnsupportedOperationException e) {
            handleException(quantity1, quantity2, "SUBTRACT", e);
            throw new QuantityMeasurementException("Subtraction not supported: " + e.getMessage(), e);
        } catch (Exception e) {
            handleException(quantity1, quantity2, "SUBTRACT", e);
            throw new QuantityMeasurementException("Subtraction failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO quantity1, QuantityDTO quantity2) {
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());

            QuantityModel<IMeasurable> model1 = new QuantityModel<>(quantity1.getValue(), unit1);
            QuantityModel<IMeasurable> model2 = new QuantityModel<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            double result = q1.divide(q2);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "DIVIDE", String.valueOf(result));
            repository.save(entity);

            return new QuantityDTO(result, "ratio", "Dimensionless");

        } catch (ArithmeticException e) {
            handleException(quantity1, quantity2, "DIVIDE", e);
            throw new QuantityMeasurementException("Division failed: " + e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            handleException(quantity1, quantity2, "DIVIDE", e);
            throw new QuantityMeasurementException("Division not supported: " + e.getMessage(), e);
        } catch (Exception e) {
            handleException(quantity1, quantity2, "DIVIDE", e);
            throw new QuantityMeasurementException("Division failed: " + e.getMessage(), e);
        }
    }

    private void validateInputs(QuantityDTO quantity1, QuantityDTO quantity2) {
        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }
    }

    private void validateSameCategory(QuantityDTO quantity1, QuantityDTO quantity2) {
        if (!quantity1.getMeasurementType().equals(quantity2.getMeasurementType())) {
            throw new IllegalArgumentException("Cannot perform operation across different measurement categories: "
                    + quantity1.getMeasurementType() + " and " + quantity2.getMeasurementType());
        }
    }

    private void handleException(QuantityDTO quantity1, QuantityDTO quantity2, String operation, Exception e) {
        try {
            IMeasurable unit1 = quantity1 != null ?
                    IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType()) : null;
            IMeasurable unit2 = quantity2 != null ?
                    IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType()) : null;

            QuantityModel<IMeasurable> model1 = quantity1 != null ?
                    new QuantityModel<>(quantity1.getValue(), unit1) : null;
            QuantityModel<IMeasurable> model2 = quantity2 != null ?
                    new QuantityModel<>(quantity2.getValue(), unit2) : null;

            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(model1, model2, operation, e.getMessage(), true);
            repository.save(errorEntity);
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repo);

        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inches = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = service.add(feet, inches);
        System.out.println("Addition result: " + result);

        QuantityDTO compareResult = service.compare(feet, inches);
        System.out.println("Comparison result: " + compareResult.getUnit());
    }
}