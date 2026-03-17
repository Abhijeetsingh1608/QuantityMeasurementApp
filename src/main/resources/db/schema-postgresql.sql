CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGSERIAL PRIMARY KEY,
    this_value DOUBLE PRECISION NOT NULL,
    this_unit VARCHAR(50) NOT NULL,
    this_measurement_type VARCHAR(50) NOT NULL,
    that_value DOUBLE PRECISION,
    that_unit VARCHAR(50),
    that_measurement_type VARCHAR(50),
    operation VARCHAR(20) NOT NULL,
    result_value DOUBLE PRECISION,
    result_unit VARCHAR(50),
    result_measurement_type VARCHAR(50),
    result_string VARCHAR(255),
    is_error BOOLEAN DEFAULT FALSE,
    error_message VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_qm_operation ON quantity_measurement_entity(operation);
CREATE INDEX IF NOT EXISTS idx_qm_measurement_type ON quantity_measurement_entity(this_measurement_type);
CREATE INDEX IF NOT EXISTS idx_qm_created_at ON quantity_measurement_entity(created_at);

CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    id BIGSERIAL PRIMARY KEY,
    entity_id BIGINT NOT NULL,
    operation_count INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qm_history_entity FOREIGN KEY (entity_id) REFERENCES quantity_measurement_entity(id)
);