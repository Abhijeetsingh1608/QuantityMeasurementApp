package com.quantityMeasurementApp.integrationTests;

import com.quantityMeasurementApp.dto.QuantityDTO;
import com.quantityMeasurementApp.controller.QuantityMeasurementController;
import com.quantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.quantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.quantityMeasurementApp.repository.QuantityMeasurementDatabaseRepository;
import com.quantityMeasurementApp.service.IQuantityMeasurementService;
import com.quantityMeasurementApp.service.QuantityMeasurementServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityMeasurementIntegrationTest {

    private IQuantityMeasurementRepository repository;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setup() {
        System.setProperty("db.url", "jdbc:h2:mem:qm_integration_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        repository = new QuantityMeasurementDatabaseRepository();
        repository.deleteAllMeasurements();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    @AfterEach
    void tearDown() {
        repository.releaseResources();
        System.clearProperty("db.url");
    }

    @Test
    void shouldPersistOperationHistoryInDatabase() {
        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        controller.compare(q1, q2);
        controller.add(q1, q2);
        controller.convert(q1, "INCHES");

        assertEquals(3, repository.getMeasurementCount());
        assertEquals(1, repository.getMeasurementsByOperation("COMPARE").size());
        assertEquals(3, repository.getMeasurementsByMeasurementType("LengthUnit").size());
    }

    @Test
    void testServiceWithDatabaseRepository_Integration() {
        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO response = controller.compare(q1, q2);

        assertEquals("Equal", response.getUnit());
        assertEquals(1, repository.getMeasurementCount());
    }

    @Test
    void testServiceWithCacheRepository_Integration() {
        QuantityMeasurementCacheRepository cacheRepository = QuantityMeasurementCacheRepository.getInstance();
        cacheRepository.deleteAllMeasurements();

        IQuantityMeasurementService cacheService = new QuantityMeasurementServiceImpl(cacheRepository);
        QuantityMeasurementController cacheController = new QuantityMeasurementController(cacheService);

        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        cacheController.compare(q1, q2);

        assertTrue(cacheRepository.getMeasurementCount() > 0);
    }
}