package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.payload.internal.PushNotificationRequestDto;
import com.c1se22.publiclaundsmartsystem.service.FCMService;
import com.c1se22.publiclaundsmartsystem.service.PushNotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {
    FCMService fcmService;
    private final Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    @Override
    public void sendPushNotificationToToken(PushNotificationRequestDto pushNotificationRequestDto) {
        fcmService.sendMessageToToken(pushNotificationRequestDto);
        logger.info("Push notification sent to token: " + pushNotificationRequestDto.getToken());
    }
}
