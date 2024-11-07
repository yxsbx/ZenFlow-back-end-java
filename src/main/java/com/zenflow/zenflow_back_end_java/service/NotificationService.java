package com.zenflow.zenflow_back_end_java.service;

import com.zenflow.zenflow_back_end_java.exception.RateLimitExceededException;
import com.zenflow.zenflow_back_end_java.limiter.RateLimiterService;
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

    @Autowired
    private RateLimiterService rateLimiterService;

    public List<NotificationDto> getAllNotifications() {
        String key = "notification:get-all";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for fetching all notifications. Please try again later.");
        }

        return notificationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public NotificationDto createNotification(NotificationDto notificationDto) {
        String key = "notification:create";
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for creating notifications. Please try again later.");
        }

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
        String key = "notification:get-by-id:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for this notification. Please try again later.");
        }

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id " + id));
        return convertToDto(notification);
    }

    public NotificationDto updateNotification(Long id, NotificationDto notificationDto) {
        String key = "notification:update:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for updating this notification. Please try again later.");
        }

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
        String key = "notification:delete:" + id;
        if (!rateLimiterService.isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests for deleting this notification. Please try again later.");
        }

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
