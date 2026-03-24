package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.dto.QuantityDTO;
import com.app.quantity_measurement_app.dto.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compareQuantities(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityMeasurementDTO convertQuantity(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityMeasurementDTO addQuantities(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityMeasurementDTO subtractQuantities(QuantityDTO quantity1, QuantityDTO quantity2);

    QuantityMeasurementDTO divideQuantities(QuantityDTO quantity1, QuantityDTO quantity2);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    long getCountByOperation(String operation);

    List<QuantityMeasurementDTO> getErrorHistory();
}
