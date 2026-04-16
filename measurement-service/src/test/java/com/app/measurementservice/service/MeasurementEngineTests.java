package com.app.measurementservice.service;

import com.app.measurementservice.domain.MeasurementCategory;
import com.app.measurementservice.domain.MeasurementOperation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeasurementEngineTests {

    private final MeasurementEngine measurementEngine = new MeasurementEngine();

    @Test
    void convertsKilometersToMiles() {
        assertEquals(6.21, measurementEngine.convert(MeasurementCategory.LENGTH, 10, "KM", "MILES"));
    }

    @Test
    void comparesEquivalentTemperatures() {
        MeasurementEngine.ComputationResult result = measurementEngine.compute(
                MeasurementCategory.TEMPERATURE,
                MeasurementOperation.COMPARE,
                100,
                "C",
                212,
                "F",
                null
        );
        assertTrue(result.comparisonResult());
    }
}
