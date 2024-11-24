package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.response.UserDto;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User", "id", id.toString()));
        return mapToUserDto(user);
    }

//    @Override
//    public UserDto addUser(UserDto userDto) {
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setFullname(userDto.getFullname());
//        user.setEmail(userDto.getEmail());
//        user.setPhone(userDto.getPhone());
//        user.setBalance(userDto.getBalance());
//        user.setCreatedAt(userDto.getCreatedAt());
//        user.setLastLoginAt(userDto.getLastLoginAt());
//        return mapToUserDto(userRepository.save(user));
//    }
//    @Override
//    public UserDto addUser(UserDto userDto) {
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(userDto.getPassword());
//        user.setFullname(userDto.getFullname());
//        user.setEmail(userDto.getEmail());
//        user.setPhone(userDto.getPhone());
//        user.setBalance(userDto.getBalance());
//        user.setIsActive(userDto.getIs_active());
//        user.setCreatedAt(userDto.getCreatedAt());
//        user.setLastLoginAt(userDto.getLastLoginAt());
//        return mapToUserDto(userRepository.save(user));
//    }

    @Override
    public UserDto updateUser(Integer id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User", "id", id.toString())
        );
        user.setUsername(userDto.getUsername());
        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setBalance(userDto.getBalance());
        user.setIsActive(userDto.getIs_active());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setLastLoginAt(userDto.getLastLoginAt());
        return mapToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User", "id", id.toString()));
        userRepository.delete(user);
    }

    private UserDto mapToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .balance(user.getBalance())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
