package com.c1se22.publiclaundsmartsystem.payload;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String email;
    private String newPassword;
    private String resetToken;
}
