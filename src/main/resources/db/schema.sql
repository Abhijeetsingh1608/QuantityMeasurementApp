DROP TABLE IF EXISTS quantity_measurement_history;
DROP TABLE IF EXISTS quantity_measurement_entity;

CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    this_value DOUBLE NOT NULL,
    this_unit VARCHAR(50) NOT NULL,
    this_measurement_type VARCHAR(50) NOT NULL,
    that_value DOUBLE,
    that_unit VARCHAR(50),
    that_measurement_type VARCHAR(50),
    operation VARCHAR(20) NOT NULL,
    result_value DOUBLE,
    result_unit VARCHAR(50),
    result_measurement_type VARCHAR(50),
    result_string VARCHAR(255),
    is_error BOOLEAN DEFAULT FALSE,
    error_message VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_qm_operation (operation),
    INDEX idx_qm_measurement_type (this_measurement_type),
    INDEX idx_qm_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id BIGINT NOT NULL,
    operation_count INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qm_history_entity
        FOREIGN KEY (entity_id) REFERENCES quantity_measurement_entity(id)
);