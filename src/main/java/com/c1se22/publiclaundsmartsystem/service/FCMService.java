package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.internal.PushNotificationRequestDto;
import com.google.firebase.messaging.Message;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FCMService {
    void sendMessage(Map<String, String> data, PushNotificationRequestDto request);
    void sendMessageWithoutData(PushNotificationRequestDto request);
    void sendMessageToToken(PushNotificationRequestDto request);
    String sendAndGetResponse(Message message);
}
