package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.NotificationDto;

import java.util.List;

public interface NotificationService {
    NotificationDto getNotification(Integer id);
    List<NotificationDto> getNotificationsByUserId(Integer userId);
    void deleteNotification(Integer id);
    void sendNotification(Integer toUserId, String message);
}
