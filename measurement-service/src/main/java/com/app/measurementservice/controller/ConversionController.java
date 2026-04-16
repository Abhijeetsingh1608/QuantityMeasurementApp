package com.app.measurementservice.controller;

import com.app.measurementservice.model.ConversionResult;
import com.app.measurementservice.service.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/convert")
public class ConversionController {

    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/length")
    public ResponseEntity<ConversionResult> convertLength(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(conversionService.convertLength(from, to, value, userId));
    }

    @GetMapping("/weight")
    public ResponseEntity<ConversionResult> convertWeight(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(conversionService.convertWeight(from, to, value, userId));
    }

    @GetMapping("/temperature")
    public ResponseEntity<ConversionResult> convertTemperature(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(conversionService.convertTemperature(from, to, value, userId));
    }

    @GetMapping("/volume")
    public ResponseEntity<ConversionResult> convertVolume(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(conversionService.convertVolume(from, to, value, userId));
    }
}
