package com.c1se22.publiclaundsmartsystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    private String phoneNumberOrEmail;
    private String password;
}
