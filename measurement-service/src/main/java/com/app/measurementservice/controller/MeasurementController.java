package com.app.measurementservice.controller;

import com.app.measurementservice.dto.ComputationRequest;
import com.app.measurementservice.dto.ConversionRequest;
import com.app.measurementservice.dto.MeasurementResponse;
import com.app.measurementservice.service.MeasurementApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementApplicationService measurementApplicationService;

    public MeasurementController(MeasurementApplicationService measurementApplicationService) {
        this.measurementApplicationService = measurementApplicationService;
    }

    @PostMapping("/convert")
    public ResponseEntity<MeasurementResponse> convert(@Valid @RequestBody ConversionRequest request) {
        return ResponseEntity.ok(measurementApplicationService.convert(request));
    }

    @PostMapping("/compute")
    public ResponseEntity<MeasurementResponse> compute(@Valid @RequestBody ComputationRequest request) {
        return ResponseEntity.ok(measurementApplicationService.compute(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<MeasurementResponse>> history() {
        return ResponseEntity.ok(measurementApplicationService.history());
    }
}
