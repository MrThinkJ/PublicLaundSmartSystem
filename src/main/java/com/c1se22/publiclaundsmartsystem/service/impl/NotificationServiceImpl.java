package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Notification;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.NotificationDto;
import com.c1se22.publiclaundsmartsystem.repository.NotificationRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
import com.c1se22.publiclaundsmartsystem.service.UserDeviceService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    NotificationRepository notificationRepository;
    UserRepository userRepository;
    UserDeviceService userDeviceService;
    FirebaseMessaging firebaseMessaging;

    @Override
    public NotificationDto getNotification(Integer id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Notification", "id", id.toString()));
        return mapToDTO(notification);
    }

    @Override
    public List<NotificationDto> getNotificationsByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString()));
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteNotification(Integer id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Notification", "id", id.toString()));
        notificationRepository.delete(notification);
    }

    @Override
    public void sendNotification(Integer toUserId, String message) {
        User toUser = userRepository.findById(toUserId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", toUserId.toString()));
        Notification notification = Notification.builder()
                .user(toUser)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .message(message)
                .build();
        notificationRepository.save(notification);
        List<String> deviceTokens = userDeviceService.getActiveUserToken(toUserId);
        deviceTokens.forEach(token ->{
            try{
                Message firebaseMessage = Message.builder()
                        .setToken(token)
                        .setNotification(
                                com.google.firebase.messaging.Notification.builder()
                                        .setTitle("System Notification")
                                        .setBody(message)
                                        .build()
                        )
                        .build();
                firebaseMessaging.send(firebaseMessage);
            } catch (FirebaseMessagingException e){
                if (e.getErrorCode().toString().equals("messaging/invalid-registration-token")) {
                    userDeviceService.deactivateDevice(token);
                }
            }
        });
    }

    private NotificationDto mapToDTO(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .build();
    }
}