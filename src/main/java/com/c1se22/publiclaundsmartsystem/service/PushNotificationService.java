package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.internal.PushNotificationRequestDto;

public interface PushNotificationService {
    void sendPushNotificationToToken(PushNotificationRequestDto pushNotificationRequestDto);
    void sendPushNotificationToTopic(PushNotificationRequestDto pushNotificationRequestDto);
}
