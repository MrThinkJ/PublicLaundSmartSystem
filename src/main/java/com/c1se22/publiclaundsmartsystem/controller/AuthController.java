package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.JwtResponse;
import com.c1se22.publiclaundsmartsystem.payload.LoginDto;
import com.c1se22.publiclaundsmartsystem.payload.LoginResponse;
import com.c1se22.publiclaundsmartsystem.payload.RegisterDto;
import com.c1se22.publiclaundsmartsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDto loginDto){
        LoginResponse loginResponse = authService.login(loginDto);
        return ResponseEntity.ok(JwtResponse.builder()
                .accessToken(loginResponse.getAccessToken())
                .userId(loginResponse.getUserId())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody @Valid RegisterDto registerDto){
        return ResponseEntity.ok(authService.register(registerDto));
    }
}
