package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.UserDto;
import com.c1se22.publiclaundsmartsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.c1se22.publiclaundsmartsystem.util.AppConstants.*;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping
    public  ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<UserDto> getUserbyId(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUserbyId(id));
    }

    @PostMapping
    public ResponseEntity <UserDto> addUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity <UserDto> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity <String> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
