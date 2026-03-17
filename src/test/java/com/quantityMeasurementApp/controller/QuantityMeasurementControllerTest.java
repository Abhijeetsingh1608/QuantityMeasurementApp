package com.quantityMeasurementApp.controller;

import com.quantityMeasurementApp.dto.QuantityDTO;
import com.quantityMeasurementApp.service.IQuantityMeasurementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class QuantityMeasurementControllerTest {

    @Test
    void shouldDelegateComparisonToService() {
        IQuantityMeasurementService service = Mockito.mock(IQuantityMeasurementService.class);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO expected = new QuantityDTO(1.0, "Equal", "Comparison");

        when(service.compare(q1, q2)).thenReturn(expected);

        QuantityDTO actual = controller.compare(q1, q2);

        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getUnit(), actual.getUnit());
    }
}