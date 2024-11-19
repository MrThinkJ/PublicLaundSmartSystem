package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.Role;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.repository.RoleRepository;
import com.c1se22.publiclaundsmartsystem.repository.UsageHistoryRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService{
    UserRepository userRepository;
    RoleRepository roleRepository;
    MachineRepository machineRepository;
    UsageHistoryRepository usageHistoryRepository;
    @Override
    public void updateUserToOwner(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()-> new ResourceNotFoundException("User", "username", username));
        Role role = roleRepository.findByName("ROLE_OWNER");
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public BigDecimal getTotalRevenue() {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        return usageHistoryRepository.sumCostByMachines(machines);
    }

    @Override
    public BigDecimal getRevenueByMonth(int month) {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public BigDecimal getRevenueByYear(int year) {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withYear(year).withMonth(1).withDayOfMonth(1);
        LocalDateTime end = start.plusYears(1).minusDays(1);
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public BigDecimal getRevenueByMonthAndYear(int month, int year) {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay();
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public Integer getNumberOfUsingByMonth(int month) {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return usageHistoryRepository.countByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public Integer getNumberOfUsingByYear(int year) {
        Integer ownerId = getOwnerId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withYear(year).withMonth(1).withDayOfMonth(1);
        LocalDateTime end = start.plusYears(1).minusDays(1);
        return usageHistoryRepository.countByMachineInAndStartTimeBetween(machines, start, end);
    }

    private Integer getOwnerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(currentUsername, currentUsername).orElseThrow(
                ()-> new ResourceNotFoundException("User", "username", "owner"));
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_OWNER"))){
            throw new ResourceNotFoundException("User", "username", "owner");
        }
        return user.getId();
    }
}
