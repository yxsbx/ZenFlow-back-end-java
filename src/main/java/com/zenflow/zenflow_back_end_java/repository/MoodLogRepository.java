package com.zenflow.zenflow_back_end_java.repository;

import com.zenflow.zenflow_back_end_java.model.MoodLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {
}
