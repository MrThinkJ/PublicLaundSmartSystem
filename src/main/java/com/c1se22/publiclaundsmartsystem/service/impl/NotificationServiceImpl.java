package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Notification;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.enums.ErrorCode;
import com.c1se22.publiclaundsmartsystem.exception.APIException;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.internal.PushNotificationRequestDto;
import com.c1se22.publiclaundsmartsystem.payload.response.NotificationDto;
import com.c1se22.publiclaundsmartsystem.repository.NotificationRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.NotificationService;
import com.c1se22.publiclaundsmartsystem.service.PushNotificationService;
import com.c1se22.publiclaundsmartsystem.service.UserDeviceService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    UserRepository userRepository;
    UserDeviceService userDeviceService;
    FirebaseMessaging firebaseMessaging;
    NotificationRepository notificationRepository;
    PushNotificationService pushNotificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationDto getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id.toString()));
    }

    @Override
    public List<NotificationDto> getNotificationsByUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
        return notificationRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<NotificationDto> getUnreadNotificationsByUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
        return notificationRepository.findByUserIdAndIsRead(user.getId(), Boolean.FALSE).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<NotificationDto> getReadNotificationsByUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
        return notificationRepository.findByUserIdAndIsRead(user.getId(), Boolean.TRUE).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markNotificationAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Notification", "id", id.toString()));
        notification.setIsRead(Boolean.TRUE);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllNotificationsAsRead(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
        notificationRepository.markAllNotificationAsRead(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendNotification(Integer toUserId, String message) {
        User toUser = userRepository.findById(toUserId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", toUserId.toString()));
        List<String> deviceTokens = userDeviceService.getActiveUserToken(toUserId);
        logger.info("Sending notification to user: "+toUser.getUsername());
        deviceTokens.forEach(token ->{
            try{
                PushNotificationRequestDto request = PushNotificationRequestDto.builder()
                        .title("System Notification")
                        .message(message)
                        .token(token)
                        .build();
                pushNotificationService.sendPushNotificationToToken(request);
                logger.info("Notification sent to device: "+token);
                Notification notification = Notification.builder()
                        .user(toUser)
                        .message(message)
                        .createdAt(LocalDateTime.now())
                        .isRead(Boolean.FALSE)
                        .title("System Notification")
                        .build();
                notificationRepository.save(notification);
                logger.info("Notification saved to database");
            } catch (Exception e){
                logger.error("Error sending notification to device: "+token);
                logger.error(e.getMessage());
                throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_ERROR);
            }
        });
    }

    @Override
    public void sendTestNotification() {
        Message msg = Message.builder()
                .setTopic("test")
                .putData("body", "some data")
                .build();
        try{
            String id = firebaseMessaging.send(msg);
            logger.info("Message sent with ID: "+id);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
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
