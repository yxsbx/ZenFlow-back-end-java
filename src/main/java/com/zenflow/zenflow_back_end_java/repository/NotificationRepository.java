package com.zenflow.zenflow_back_end_java.repository;

import com.zenflow.zenflow_back_end_java.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
