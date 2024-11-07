package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.*;

public interface OTPService {
    void sendOTP(String email, String otp);
    OTPResponseDto sendOTP(EmailRequestDto emailRequestDto);
    VerifyOTPResponseDto verifyOTP(VerifyOTPRequestDto verifyOTPRequestDto);
    boolean resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
}
