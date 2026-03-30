package com.app.quantity_measurement_app.repository;

import com.app.quantity_measurement_app.model.QuantityMeasurementEntity;
import com.app.quantity_measurement_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = :operation AND q.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(@Param("operation") String operation);

    long countByOperationAndIsErrorFalse(String operation);

    List<QuantityMeasurementEntity> findByIsErrorTrue();

    List<QuantityMeasurementEntity> findByUser(User user);

    List<QuantityMeasurementEntity> findByUserAndOperation(User user, String operation);

    List<QuantityMeasurementEntity> findByUserAndThisMeasurementType(User user, String measurementType);

    long countByUserAndOperationAndIsErrorFalse(User user, String operation);

    List<QuantityMeasurementEntity> findByUserAndIsErrorTrue(User user);

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.user.id = :userId ORDER BY q.createdAt DESC")
    List<QuantityMeasurementEntity> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
