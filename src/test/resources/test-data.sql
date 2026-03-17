INSERT INTO quantity_measurement_entity (
    this_value, this_unit, this_measurement_type,
    that_value, that_unit, that_measurement_type,
    operation, result_value, result_unit, result_measurement_type,
    result_string, is_error, error_message
) VALUES
(1.0, 'FEET', 'LengthUnit', 12.0, 'INCHES', 'LengthUnit', 'COMPARE', NULL, NULL, NULL, 'Equal', FALSE, NULL);