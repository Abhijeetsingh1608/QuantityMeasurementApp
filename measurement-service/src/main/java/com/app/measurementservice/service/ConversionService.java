package com.app.measurementservice.service;

import com.app.measurementservice.client.UserServiceClient;
import com.app.measurementservice.domain.MeasurementCategory;
import com.app.measurementservice.domain.MeasurementOperation;
import com.app.measurementservice.dto.ConversionHistoryRequest;
import com.app.measurementservice.dto.ConversionHistoryResponse;
import com.app.measurementservice.entity.MeasurementRecord;
import com.app.measurementservice.model.ConversionResult;
import com.app.measurementservice.repository.MeasurementRecordRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ConversionService {

    private final MeasurementEngine measurementEngine;
    private final MeasurementRecordRepository measurementRecordRepository;
    private final UserServiceClient userServiceClient;

    public ConversionService(
            MeasurementEngine measurementEngine,
            MeasurementRecordRepository measurementRecordRepository,
            UserServiceClient userServiceClient
    ) {
        this.measurementEngine = measurementEngine;
        this.measurementRecordRepository = measurementRecordRepository;
        this.userServiceClient = userServiceClient;
    }

    public ConversionResult convertLength(String from, String to, double value, Long userId) {
        return convert(MeasurementCategory.LENGTH, from, to, value, userId);
    }

    public ConversionResult convertWeight(String from, String to, double value, Long userId) {
        return convert(MeasurementCategory.WEIGHT, from, to, value, userId);
    }

    public ConversionResult convertTemperature(String from, String to, double value, Long userId) {
        return convert(MeasurementCategory.TEMPERATURE, from, to, value, userId);
    }

    public ConversionResult convertVolume(String from, String to, double value, Long userId) {
        return convert(MeasurementCategory.VOLUME, from, to, value, userId);
    }

    private ConversionResult convert(MeasurementCategory category, String from, String to, double value, Long userId) {
        double convertedValue = measurementEngine.convert(category, value, from, to);
        Instant recordedAt = Instant.now();

        MeasurementRecord record = new MeasurementRecord();
        record.setUserId(userId);
        record.setCategory(category);
        record.setOperation(MeasurementOperation.CONVERT);
        record.setInputValue(value);
        record.setInputUnit(from.toUpperCase());
        record.setResultValue(convertedValue);
        record.setResultUnit(to.toUpperCase());
        record.setMessage("Conversion completed");
        record.setRecordedAt(recordedAt);
        measurementRecordRepository.save(record);

        saveUserHistory(userId, record);

        return new ConversionResult(from.toLowerCase(), to.toLowerCase(), value, convertedValue);
    }

    public List<ConversionHistoryResponse> getUserHistory(Long userId) {
        return userServiceClient.getHistory(userId);
    }

    private ConversionHistoryResponse saveUserHistory(Long userId, MeasurementRecord record) {
        if (userId == null) {
            return ConversionHistoryResponse.empty();
        }

        return userServiceClient.saveHistory(userId, new ConversionHistoryRequest(
                record.getCategory(),
                record.getOperation(),
                record.getInputValue(),
                record.getInputUnit(),
                record.getSecondaryValue(),
                record.getSecondaryUnit(),
                record.getResultValue(),
                record.getResultUnit(),
                record.getComparisonResult(),
                record.getMessage()
        ));
    }
}
