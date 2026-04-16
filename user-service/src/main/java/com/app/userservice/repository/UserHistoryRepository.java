package com.app.userservice.repository;

import com.app.userservice.entity.UserHistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistoryRecord, Long> {
    List<UserHistoryRecord> findByUserIdOrderByRecordedAtDesc(Long userId);
}
