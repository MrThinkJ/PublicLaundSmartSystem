package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Role;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.enums.ErrorCode;
import com.c1se22.publiclaundsmartsystem.exception.APIException;
import com.c1se22.publiclaundsmartsystem.payload.LoginDto;
import com.c1se22.publiclaundsmartsystem.payload.LoginResponse;
import com.c1se22.publiclaundsmartsystem.payload.RegisterDto;
import com.c1se22.publiclaundsmartsystem.repository.RoleRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.security.JwtProvider;
import com.c1se22.publiclaundsmartsystem.service.AuthService;
import lombok.AllArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    AuthenticationManager authenticationManager;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumberOrEmail(), loginDto.getPassword())
        );
        String token = jwtProvider.generateToken(authentication);
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with phone username or email: "+username));
        return LoginResponse.builder()
                .accessToken(token)
                .userId(user.getId())
                .build();
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        User user = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail()).orElse(null);
        if (user != null) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.EXISTING_USERNAME_OR_EMAIL);
        }
        user = userRepository.findByPhone(registerDto.getPhone()).orElse(null);
        if (user != null) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.EXISTING_PHONE);
        }
        user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .balance(BigDecimal.valueOf(0))
                .createdAt(LocalDate.now())
                .lastLoginAt(LocalDateTime.now())
                .build();
        Set<Role> roles = Set.of(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }
}
