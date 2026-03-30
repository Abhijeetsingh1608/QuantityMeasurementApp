package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.dto.QuantityDTO;
import com.app.quantity_measurement_app.dto.QuantityMeasurementDTO;
import com.app.quantity_measurement_app.model.User;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compareQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user);

    QuantityMeasurementDTO convertQuantity(QuantityDTO quantity1, QuantityDTO quantity2, User user);

    QuantityMeasurementDTO addQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user);

    QuantityMeasurementDTO subtractQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user);

    QuantityMeasurementDTO divideQuantities(QuantityDTO quantity1, QuantityDTO quantity2, User user);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    long getCountByOperation(String operation);

    List<QuantityMeasurementDTO> getErrorHistory();

    List<QuantityMeasurementDTO> getUserHistory(User user);

    List<QuantityMeasurementDTO> getUserHistoryByOperation(User user, String operation);

    List<QuantityMeasurementDTO> getUserHistoryByMeasurementType(User user, String measurementType);

    long getUserCountByOperation(User user, String operation);

    List<QuantityMeasurementDTO> getUserErrorHistory(User user);
}
