package com.app.userservice.service;

import com.app.userservice.dto.CreateUserRequest;
import com.app.userservice.dto.UserHistoryRequest;
import com.app.userservice.dto.UserHistoryResponse;
import com.app.userservice.dto.UserResponse;
import com.app.userservice.entity.UserHistoryRecord;
import com.app.userservice.entity.UserProfile;
import com.app.userservice.repository.UserHistoryRepository;
import com.app.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserApplicationService {

    private final UserProfileRepository userProfileRepository;
    private final UserHistoryRepository userHistoryRepository;

    public UserApplicationService(UserProfileRepository userProfileRepository, UserHistoryRepository userHistoryRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userHistoryRepository = userHistoryRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {
        UserProfile profile = new UserProfile();
        profile.setName(request.name());
        profile.setEmail(request.email());
        profile.setCreatedAt(Instant.now());
        return toResponse(userProfileRepository.save(profile));
    }

    public UserResponse getUser(Long userId) {
        return toResponse(userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId)));
    }

    public UserHistoryResponse addHistory(Long userId, UserHistoryRequest request) {
        ensureUserExists(userId);
        UserHistoryRecord record = new UserHistoryRecord();
        record.setUserId(userId);
        record.setCategory(request.category());
        record.setOperation(request.operation());
        record.setInputValue(request.inputValue());
        record.setInputUnit(request.inputUnit());
        record.setSecondaryValue(request.secondaryValue());
        record.setSecondaryUnit(request.secondaryUnit());
        record.setResultValue(request.resultValue());
        record.setResultUnit(request.resultUnit());
        record.setComparisonResult(request.comparisonResult());
        record.setMessage(request.message());
        record.setRecordedAt(Instant.now());
        return toHistoryResponse(userHistoryRepository.save(record));
    }

    public List<UserHistoryResponse> getHistory(Long userId) {
        ensureUserExists(userId);
        return userHistoryRepository.findByUserIdOrderByRecordedAtDesc(userId)
                .stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    private void ensureUserExists(Long userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }

    private UserResponse toResponse(UserProfile profile) {
        return new UserResponse(profile.getId(), profile.getName(), profile.getEmail(), profile.getCreatedAt());
    }

    private UserHistoryResponse toHistoryResponse(UserHistoryRecord record) {
        return new UserHistoryResponse(
                record.getId(),
                record.getUserId(),
                record.getCategory(),
                record.getOperation(),
                record.getInputValue(),
                record.getInputUnit(),
                record.getSecondaryValue(),
                record.getSecondaryUnit(),
                record.getResultValue(),
                record.getResultUnit(),
                record.getComparisonResult(),
                record.getMessage(),
                record.getRecordedAt()
        );
    }
}
