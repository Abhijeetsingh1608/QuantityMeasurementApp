package com.app.measurementservice.client;

import com.app.measurementservice.dto.ConversionHistoryRequest;
import com.app.measurementservice.dto.ConversionHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @PostMapping("/api/users/{userId}/history")
    ConversionHistoryResponse saveHistory(
            @PathVariable("userId") Long userId,
            @RequestBody ConversionHistoryRequest request
    );

    @GetMapping("/api/users/{userId}/history")
    List<ConversionHistoryResponse> getHistory(@PathVariable("userId") Long userId);
}
