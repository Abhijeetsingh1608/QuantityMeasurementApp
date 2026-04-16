package com.app.measurementservice.client;

import com.app.measurementservice.dto.ConversionHistoryRequest;
import com.app.measurementservice.dto.ConversionHistoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientFallback.class);

    @Override
    public ConversionHistoryResponse saveHistory(Long userId, ConversionHistoryRequest request) {
        logger.warn("user-service unavailable - history not saved for user {}", userId);
        return ConversionHistoryResponse.empty();
    }

    @Override
    public List<ConversionHistoryResponse> getHistory(Long userId) {
        logger.warn("user-service unavailable - history lookup skipped for user {}", userId);
        return List.of();
    }
}
