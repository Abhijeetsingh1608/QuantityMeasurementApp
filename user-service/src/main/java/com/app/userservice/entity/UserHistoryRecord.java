package com.app.userservice.entity;

import com.app.userservice.domain.MeasurementCategory;
import com.app.userservice.domain.MeasurementOperation;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_history_records")
public class UserHistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private MeasurementCategory category;

    @Enumerated(EnumType.STRING)
    private MeasurementOperation operation;

    private Double inputValue;
    private String inputUnit;
    private Double secondaryValue;
    private String secondaryUnit;
    private Double resultValue;
    private String resultUnit;
    private Boolean comparisonResult;
    private String message;
    private Instant recordedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MeasurementCategory getCategory() {
        return category;
    }

    public void setCategory(MeasurementCategory category) {
        this.category = category;
    }

    public MeasurementOperation getOperation() {
        return operation;
    }

    public void setOperation(MeasurementOperation operation) {
        this.operation = operation;
    }

    public Double getInputValue() {
        return inputValue;
    }

    public void setInputValue(Double inputValue) {
        this.inputValue = inputValue;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public void setInputUnit(String inputUnit) {
        this.inputUnit = inputUnit;
    }

    public Double getSecondaryValue() {
        return secondaryValue;
    }

    public void setSecondaryValue(Double secondaryValue) {
        this.secondaryValue = secondaryValue;
    }

    public String getSecondaryUnit() {
        return secondaryUnit;
    }

    public void setSecondaryUnit(String secondaryUnit) {
        this.secondaryUnit = secondaryUnit;
    }

    public Double getResultValue() {
        return resultValue;
    }

    public void setResultValue(Double resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public void setComparisonResult(Boolean comparisonResult) {
        this.comparisonResult = comparisonResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }
}
