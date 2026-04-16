package com.app.userservice.controller;

import com.app.userservice.dto.UserHistoryRequest;
import com.app.userservice.dto.UserHistoryResponse;
import com.app.userservice.service.UserApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class HistoryController {

    private final UserApplicationService userApplicationService;

    public HistoryController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping("/{userId}/history")
    public ResponseEntity<UserHistoryResponse> saveHistory(
            @PathVariable Long userId,
            @Valid @RequestBody UserHistoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userApplicationService.addHistory(userId, request));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<UserHistoryResponse>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(userApplicationService.getHistory(userId));
    }
}
