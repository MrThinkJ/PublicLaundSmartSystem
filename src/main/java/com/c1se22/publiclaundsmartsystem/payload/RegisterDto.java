package com.c1se22.publiclaundsmartsystem.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String phone;
}
