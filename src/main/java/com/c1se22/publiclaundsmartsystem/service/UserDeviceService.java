package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.UserDeviceRegisterDto;
import com.c1se22.publiclaundsmartsystem.payload.UserDeviceResponseDto;

import java.util.List;

public interface UserDeviceService {
    void registerDevice(UserDeviceRegisterDto userDeviceRegisterDto);
    List<String> getActiveUserToken(Integer userId);
    List<UserDeviceResponseDto> getDeviceByUserId(Integer userId);
    boolean isDeviceActive(String fcmToken);
    boolean deactivateDevice(String fcmToken);
}