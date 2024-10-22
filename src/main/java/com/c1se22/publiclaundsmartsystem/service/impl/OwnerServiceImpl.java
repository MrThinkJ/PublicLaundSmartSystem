package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Role;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.repository.RoleRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService{
    UserRepository userRepository;
    RoleRepository roleRepository;
    @Override
    public void updateUserToOwner(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()-> new ResourceNotFoundException("User", "username", username));
        Role role = roleRepository.findByName("ROLE_OWNER");
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
