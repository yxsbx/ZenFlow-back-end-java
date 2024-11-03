package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.dto.NotificationDto;
import com.zenflow.zenflow_back_end_java.exception.NotificationNotFoundException;
import com.zenflow.zenflow_back_end_java.model.Notification;
import com.zenflow.zenflow_back_end_java.model.Users;
import com.zenflow.zenflow_back_end_java.repository.NotificationRepository;
import com.zenflow.zenflow_back_end_java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<NotificationDto> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public NotificationDto createNotification(NotificationDto notificationDto) {
        Users user = userRepository.findById(notificationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUsers(user);
        notification.setType(notificationDto.getType());
        notification.setMessage(notificationDto.getMessage());
        notification.setScheduledTime(notificationDto.getScheduledTime());

        Notification savedNotification = notificationRepository.save(notification);
        return convertToDto(savedNotification);
    }

    public NotificationDto getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id " + id));
        return convertToDto(notification);
    }

    public NotificationDto updateNotification(Long id, NotificationDto notificationDto) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id " + id));

        Users user = userRepository.findById(notificationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        notification.setUsers(user);
        notification.setType(notificationDto.getType());
        notification.setMessage(notificationDto.getMessage());
        notification.setScheduledTime(notificationDto.getScheduledTime());
        notification.setUpdatedAt(LocalDateTime.now());

        Notification updatedNotification = notificationRepository.save(notification);
        return convertToDto(updatedNotification);
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id " + id));
        notification.setDeletedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    private NotificationDto convertToDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUsers().getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getScheduledTime()
        );
    }
}
