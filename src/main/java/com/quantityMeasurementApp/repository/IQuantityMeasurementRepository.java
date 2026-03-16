package com.quantityMeasurementApp.repository;

import com.quantityMeasurementApp.model.QuantityMeasurementEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);

    List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType);

    default List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return getMeasurementsByMeasurementType(measurementType);
    }

    long getMeasurementCount();

    default long getTotalCount() {
        return getMeasurementCount();
    }

    void deleteAllMeasurements();

    default void deleteAll() {
        deleteAllMeasurements();
    }

    default Map<String, Integer> getPoolStatistics() {
        return Collections.emptyMap();
    }

    default void releaseResources() {
    }

    public static void main(String[] args) {
        System.out.println("Testing IQuantityMeasurementRepository interface");
    }
}