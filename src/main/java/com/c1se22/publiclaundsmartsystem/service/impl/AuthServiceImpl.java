package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Role;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.entity.UserBanHistory;
import com.c1se22.publiclaundsmartsystem.enums.ErrorCode;
import com.c1se22.publiclaundsmartsystem.enums.RoleEnum;
import com.c1se22.publiclaundsmartsystem.exception.APIException;
import com.c1se22.publiclaundsmartsystem.payload.LoginDto;
import com.c1se22.publiclaundsmartsystem.payload.LoginResponse;
import com.c1se22.publiclaundsmartsystem.payload.RegisterDto;
import com.c1se22.publiclaundsmartsystem.payload.UserDto;
import com.c1se22.publiclaundsmartsystem.repository.RoleRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserBanHistoryRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.security.JwtProvider;
import com.c1se22.publiclaundsmartsystem.service.AuthService;
import com.c1se22.publiclaundsmartsystem.service.UserBanService;
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
    UserBanService userBanService;
    UserBanHistoryRepository userBanHistoryRepository;

    @Override
    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
        );
        String token = jwtProvider.generateToken(authentication);
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with phone username or email: "+username));
        if (!user.getIsActive()){
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.USER_DELETED, username);
        }
        if (userBanService.isUserBanned(user.getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.USER_BANNED, username);
        }
        return LoginResponse.builder()
                .accessToken(token)
                .userId(user.getId())
                .build();
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.PASSWORD_DOES_NOT_MATCH);
        }
        User user = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail()).orElse(null);
        if (user != null) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.EXISTING_USERNAME_OR_EMAIL);
        }
        user = userRepository.findByPhone(registerDto.getPhone()).orElse(null);
        if (user != null) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.EXISTING_PHONE, registerDto.getPhone());
        }
        user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .fullname(registerDto.getFullname())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .balance(BigDecimal.valueOf(0))
                .createdAt(LocalDate.now())
                .lastLoginAt(LocalDateTime.now())
                .build();
        Set<Role> roles = Set.of(roleRepository.findByName(RoleEnum.ROLE_ADMIN.name()));
        user.setRoles(roles);
        userRepository.save(user);
        UserBanHistory userBanHistory = UserBanHistory.builder()
                .user(user)
                .build();
        userBanHistoryRepository.save(userBanHistory);
        return true;
    }

    @Override
    public UserDto me(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with phone username or email: "+username));
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fullname(user.getFullname())
                .balance(user.getBalance())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
