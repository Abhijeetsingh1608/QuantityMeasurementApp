package com.app.measurementservice.repository;

import com.app.measurementservice.entity.MeasurementRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRecordRepository extends JpaRepository<MeasurementRecord, Long> {
}
