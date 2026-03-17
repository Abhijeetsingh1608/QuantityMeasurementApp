package com.quantityMeasurementApp.service;

import com.quantityMeasurementApp.dto.QuantityDTO;
import com.quantityMeasurementApp.repository.IQuantityMeasurementRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class QuantityMeasurementServiceImplTest {

    @Test
    void shouldCompareEquivalentLengthQuantities() {
        IQuantityMeasurementRepository repository = Mockito.mock(IQuantityMeasurementRepository.class);
        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);

        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = service.compare(q1, q2);

        assertEquals(1.0, result.getValue());
        assertEquals("Equal", result.getUnit());
        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldConvertLengthUnit() {
        IQuantityMeasurementRepository repository = Mockito.mock(IQuantityMeasurementRepository.class);
        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);

        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = service.convert(q1, "INCHES");

        assertEquals(12.0, result.getValue());
        assertEquals("INCHES", result.getUnit());
        verify(repository, times(1)).save(any());
    }
}