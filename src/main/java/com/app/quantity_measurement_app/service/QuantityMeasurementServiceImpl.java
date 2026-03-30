package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.Quantity;
import com.app.quantity_measurement_app.dto.QuantityDTO;
import com.app.quantity_measurement_app.dto.QuantityMeasurementDTO;
import com.app.quantity_measurement_app.exception.QuantityMeasurementException;
import com.app.quantity_measurement_app.model.QuantityMeasurementEntity;
import com.app.quantity_measurement_app.model.User;
import com.app.quantity_measurement_app.repository.QuantityMeasurementRepository;
import com.app.quantity_measurement_app.unit.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    @Autowired
    private QuantityMeasurementRepository repository;

    @Override
    public QuantityMeasurementDTO compareQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(user);
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            boolean isEqual = q1.equals(q2);

            populateEntity(entity, quantity1, quantity2, "compare");
            entity.setResultString(String.valueOf(isEqual));
            entity.setError(false);
            repository.save(entity);
            logger.debug("COMPARE persisted for user: {}", user != null ? user.getEmail() : "anonymous");

            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            persistError(entity, quantity1, quantity2, "compare", e);
            throw new QuantityMeasurementException("compare Error: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO convertQuantity(QuantityDTO quantity1, QuantityDTO quantity2, User user) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(user);
        try {
            if (quantity1 == null) {
                throw new IllegalArgumentException("Source quantity cannot be null");
            }

            IMeasurable sourceUnit = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable targetUnit = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());

            double convertedValue = Quantity.convert(quantity1.getValue(), sourceUnit, targetUnit);

            populateEntity(entity, quantity1, quantity2, "convert");
            entity.setResultValue(convertedValue);
            entity.setError(false);
            repository.save(entity);
            logger.debug("CONVERT persisted for user: {}", user != null ? user.getEmail() : "anonymous");

            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            persistError(entity, quantity1, quantity2, "convert", e);
            throw new QuantityMeasurementException("convert Error: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO addQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(user);
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());
            IMeasurable targetUnitObj = unit1;

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> result = q1.add(q2, targetUnitObj);

            populateEntity(entity, quantity1, quantity2, "add");
            entity.setResultValue(result.getValue());
            entity.setResultUnit(result.getUnit().getUnitName());
            entity.setResultMeasurementType(result.getUnit().getMeasurementType());
            entity.setError(false);
            repository.save(entity);
            logger.debug("ADD persisted for user: {}", user != null ? user.getEmail() : "anonymous");

            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            persistError(entity, quantity1, quantity2, "add", e);
            throw new QuantityMeasurementException("add Error: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO subtractQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(user);
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());
            IMeasurable targetUnitObj = unit1;

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            Quantity<IMeasurable> result = q1.subtract(q2, targetUnitObj);

            populateEntity(entity, quantity1, quantity2, "subtract");
            entity.setResultValue(result.getValue());
            entity.setResultUnit(result.getUnit().getUnitName());
            entity.setResultMeasurementType(result.getUnit().getMeasurementType());
            entity.setError(false);
            repository.save(entity);
            logger.debug("SUBTRACT persisted for user: {}", user != null ? user.getEmail() : "anonymous");

            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (Exception e) {
            persistError(entity, quantity1, quantity2, "subtract", e);
            throw new QuantityMeasurementException("subtract Error: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO divideQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(user);
        try {
            validateInputs(quantity1, quantity2);
            validateSameCategory(quantity1, quantity2);

            IMeasurable unit1 = IMeasurable.getUnitByName(quantity1.getUnit(), quantity1.getMeasurementType());
            IMeasurable unit2 = IMeasurable.getUnitByName(quantity2.getUnit(), quantity2.getMeasurementType());

            Quantity<IMeasurable> q1 = new Quantity<>(quantity1.getValue(), unit1);
            Quantity<IMeasurable> q2 = new Quantity<>(quantity2.getValue(), unit2);

            double result = q1.divide(q2);

            populateEntity(entity, quantity1, quantity2, "divide");
            entity.setResultValue(result);
            entity.setError(false);
            repository.save(entity);
            logger.debug("DIVIDE persisted for user: {}", user != null ? user.getEmail() : "anonymous");

            return QuantityMeasurementDTO.fromEntity(entity);

        } catch (ArithmeticException e) {
            persistError(entity, quantity1, quantity2, "divide", e);
            throw e;
        } catch (Exception e) {
            persistError(entity, quantity1, quantity2, "divide", e);
            throw new QuantityMeasurementException("divide Error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        List<QuantityMeasurementEntity> entities = repository.findByOperation(operation);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        List<QuantityMeasurementEntity> entities = repository.findByThisMeasurementType(measurementType);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public long getCountByOperation(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        List<QuantityMeasurementEntity> entities = repository.findByIsErrorTrue();
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getUserHistory(User user) {
        List<QuantityMeasurementEntity> entities = repository.findByUser(user);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getUserHistoryByOperation(User user, String operation) {
        List<QuantityMeasurementEntity> entities = repository.findByUserAndOperation(user, operation);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getUserHistoryByMeasurementType(User user, String measurementType) {
        List<QuantityMeasurementEntity> entities = repository.findByUserAndThisMeasurementType(user, measurementType);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public long getUserCountByOperation(User user, String operation) {
        return repository.countByUserAndOperationAndIsErrorFalse(user, operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getUserErrorHistory(User user) {
        List<QuantityMeasurementEntity> entities = repository.findByUserAndIsErrorTrue(user);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    private void validateInputs(QuantityDTO quantity1, QuantityDTO quantity2) {
        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }
    }

    private void validateSameCategory(QuantityDTO quantity1, QuantityDTO quantity2) {
        if (!quantity1.getMeasurementType().equals(quantity2.getMeasurementType())) {
            throw new IllegalArgumentException("Cannot perform arithmetic between different measurement categories: "
                    + quantity1.getMeasurementType() + " and " + quantity2.getMeasurementType());
        }
    }

    private void populateEntity(QuantityMeasurementEntity entity, QuantityDTO q1, QuantityDTO q2, String operation) {
        if (q1 != null) {
            entity.setThisValue(q1.getValue());
            entity.setThisUnit(q1.getUnit());
            entity.setThisMeasurementType(q1.getMeasurementType());
        }
        if (q2 != null) {
            entity.setThatValue(q2.getValue());
            entity.setThatUnit(q2.getUnit());
            entity.setThatMeasurementType(q2.getMeasurementType());
        }
        entity.setOperation(operation);
    }

    private void persistError(QuantityMeasurementEntity entity, QuantityDTO q1, QuantityDTO q2, String operation, Exception e) {
        try {
            populateEntity(entity, q1, q2, operation);
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            logger.warn("Operation {} failed and was persisted as error: {}", operation, e.getMessage());
        } catch (Exception ignored) {
            logger.warn("Failed to persist operation error for {}", operation);
        }
    }
}
