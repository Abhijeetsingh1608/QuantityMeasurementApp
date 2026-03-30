package com.app.quantity_measurement_app.controller;

import com.app.quantity_measurement_app.dto.QuantityInputDTO;
import com.app.quantity_measurement_app.dto.QuantityMeasurementDTO;
import com.app.quantity_measurement_app.model.User;
import com.app.quantity_measurement_app.security.CustomUserPrincipal;
import com.app.quantity_measurement_app.service.IUserService;
import com.app.quantity_measurement_app.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    @Autowired
    private IQuantityMeasurementService service;

    @Autowired
    private IUserService userService;

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(
            @Valid @RequestBody QuantityInputDTO input,
            Authentication authentication) {
        User user = resolveUser(authentication);
        logger.info("POST /compare by user: {}", user != null ? user.getEmail() : "anonymous");
        QuantityMeasurementDTO result = service.compareQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO(), user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to a target unit")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(
            @Valid @RequestBody QuantityInputDTO input,
            Authentication authentication) {
        User user = resolveUser(authentication);
        logger.info("POST /convert by user: {}", user != null ? user.getEmail() : "anonymous");
        QuantityMeasurementDTO result = service.convertQuantity(
                input.getThisQuantityDTO(), input.getThatQuantityDTO(), user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(
            @Valid @RequestBody QuantityInputDTO input,
            Authentication authentication) {
        User user = resolveUser(authentication);
        logger.info("POST /add by user: {}", user != null ? user.getEmail() : "anonymous");
        QuantityMeasurementDTO result = service.addQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO(), user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(
            @Valid @RequestBody QuantityInputDTO input,
            Authentication authentication) {
        User user = resolveUser(authentication);
        logger.info("POST /subtract by user: {}", user != null ? user.getEmail() : "anonymous");
        QuantityMeasurementDTO result = service.subtractQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO(), user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(
            @Valid @RequestBody QuantityInputDTO input,
            Authentication authentication) {
        User user = resolveUser(authentication);
        logger.info("POST /divide by user: {}", user != null ? user.getEmail() : "anonymous");
        QuantityMeasurementDTO result = service.divideQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO(), user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get measurement history by operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(@PathVariable String operation) {
        logger.info("GET /history/operation/{}", operation);
        List<QuantityMeasurementDTO> history = service.getHistoryByOperation(operation);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get measurement history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementTypeHistory(@PathVariable String measurementType) {
        logger.info("GET /history/type/{}", measurementType);
        List<QuantityMeasurementDTO> history = service.getHistoryByMeasurementType(measurementType);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get count of successful operations by type")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        logger.info("GET /count/{}", operation);
        long count = service.getCountByOperation(operation);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get error history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        logger.info("GET /history/errored");
        List<QuantityMeasurementDTO> errors = service.getErrorHistory();
        return ResponseEntity.ok(errors);
    }

    @GetMapping("/my/history")
    @Operation(summary = "Get current user's measurement history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMyHistory(
            Authentication authentication) {
        User user = resolveUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        logger.info("GET /my/history for user: {}", user.getEmail());
        List<QuantityMeasurementDTO> history = service.getUserHistory(user);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/my/history/operation/{operation}")
    @Operation(summary = "Get current user's history by operation")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMyOperationHistory(
            @PathVariable String operation,
            Authentication authentication) {
        User user = resolveUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        logger.info("GET /my/history/operation/{} for user: {}", operation, user.getEmail());
        List<QuantityMeasurementDTO> history = service.getUserHistoryByOperation(user, operation);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/my/history/type/{measurementType}")
    @Operation(summary = "Get current user's history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMyTypeHistory(
            @PathVariable String measurementType,
            Authentication authentication) {
        User user = resolveUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        logger.info("GET /my/history/type/{} for user: {}", measurementType, user.getEmail());
        List<QuantityMeasurementDTO> history = service.getUserHistoryByMeasurementType(user, measurementType);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/my/count/{operation}")
    @Operation(summary = "Get current user's count by operation")
    public ResponseEntity<Long> getMyOperationCount(
            @PathVariable String operation,
            Authentication authentication) {
        User user = resolveUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        logger.info("GET /my/count/{} for user: {}", operation, user.getEmail());
        long count = service.getUserCountByOperation(user, operation);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/my/history/errored")
    @Operation(summary = "Get current user's error history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMyErrorHistory(
            Authentication authentication) {
        User user = resolveUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        logger.info("GET /my/history/errored for user: {}", user.getEmail());
        List<QuantityMeasurementDTO> errors = service.getUserErrorHistory(user);
        return ResponseEntity.ok(errors);
    }

    private User resolveUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserPrincipal customUserPrincipal) {
            return customUserPrincipal.getUser();
        }
        if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            if (email != null) {
                return userService.findByEmail(email).orElse(null);
            }
        }
        return null;
    }
}
