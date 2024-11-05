package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.LoginDto;
import com.c1se22.publiclaundsmartsystem.payload.LoginResponse;
import com.c1se22.publiclaundsmartsystem.payload.RegisterDto;

public interface AuthService {
    LoginResponse login(LoginDto loginDto);
    boolean register(RegisterDto registerDto);
}
