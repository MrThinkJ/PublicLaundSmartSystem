package com.c1se22.publiclaundsmartsystem.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OTPResponseDto {
    private boolean isSuccess;
    private String message;
}
