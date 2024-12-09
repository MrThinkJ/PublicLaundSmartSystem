package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.*;
import com.c1se22.publiclaundsmartsystem.enums.ErrorCode;
import com.c1se22.publiclaundsmartsystem.enums.TransactionStatus;
import com.c1se22.publiclaundsmartsystem.enums.TransactionType;
import com.c1se22.publiclaundsmartsystem.exception.APIException;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import com.c1se22.publiclaundsmartsystem.payload.request.OwnerWithdrawInfoRequestDto;
import com.c1se22.publiclaundsmartsystem.repository.*;
import com.c1se22.publiclaundsmartsystem.service.OwnerService;
import com.c1se22.publiclaundsmartsystem.util.AppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OwnerServiceImpl implements OwnerService{
    UserRepository userRepository;
    RoleRepository roleRepository;
    MachineRepository machineRepository;
    UsageHistoryRepository usageHistoryRepository;
    OwnerWithdrawInfoRepository ownerWithdrawInfoRepository;
    TransactionRepository transactionRepository;
    @Override
    public void updateUserToOwner(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()-> new ResourceNotFoundException("User", "username", username));
        Role role = roleRepository.findByName("ROLE_OWNER").orElseThrow(
                ()-> new ResourceNotFoundException("Role", "name", "ROLE_OWNER"));
        user.getRoles().add(role);
        userRepository.save(user);
        log.info("User {} is updated to owner", username);
    }

    @Override
    public BigDecimal getTotalRevenue() {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        return usageHistoryRepository.sumCostByMachines(machines);
    }

    @Override
    public BigDecimal getRevenueBeforeDate(LocalDate date) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime end = date.atStartOfDay();
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBefore(machines, end);
    }

    @Override
    public BigDecimal getRevenueByMonth(int month) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public BigDecimal getRevenueByYear(int year) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withYear(year).withMonth(1).withDayOfMonth(1);
        LocalDateTime end = start.plusYears(1).minusDays(1);
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public BigDecimal getRevenueByMonthAndYear(int month, int year) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay();
        return usageHistoryRepository.sumCostByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public Integer getNumberOfUsingByMonth(int month) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return usageHistoryRepository.countByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public Integer getNumberOfUsingByYear(int year) {
        Integer ownerId = getOwner().getId();
        List<Machine> machines = machineRepository.findMachinesByOwnerId(ownerId);
        LocalDateTime start = LocalDateTime.now().withYear(year).withMonth(1).withDayOfMonth(1);
        LocalDateTime end = start.plusYears(1).minusDays(1);
        return usageHistoryRepository.countByMachineInAndStartTimeBetween(machines, start, end);
    }

    @Override
    public Boolean updateWithdrawInfo(OwnerWithdrawInfoRequestDto requestDto) {
        User user = getOwner();
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_OWNER"))){
            throw new APIException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN);
        }
        ownerWithdrawInfoRepository.findByOwnerUsername(user.getUsername()).ifPresentOrElse(
                ownerWithdrawInfo -> {
                    ownerWithdrawInfo.setBankName(requestDto.getBankName());
                    ownerWithdrawInfo.setAccountNumber(requestDto.getAccountNumber());
                    ownerWithdrawInfo.setAccountName(requestDto.getAccountName());
                    ownerWithdrawInfoRepository.save(ownerWithdrawInfo);
                },
                ()-> ownerWithdrawInfoRepository.save(OwnerWithdrawInfo.builder()
                        .bankName(requestDto.getBankName())
                        .accountNumber(requestDto.getAccountNumber())
                        .accountName(requestDto.getAccountName())
                        .lastWithdrawDate(null)
                        .owner(user)
                        .build())
        );
        return true;
    }

    private BigDecimal getAmountCanWithdraw(String username) {
        OwnerWithdrawInfo ownerWithdrawInfo = ownerWithdrawInfoRepository.findByOwnerUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("OwnerWithdrawInfo", "ownerUsername", username));
        BigDecimal totalRevenue;
        if (ownerWithdrawInfo.getLastWithdrawDate() == null)
            totalRevenue = getRevenueBeforeDate(LocalDate.now());
        else
            totalRevenue = getRevenueByDateRange(ownerWithdrawInfo.getLastWithdrawDate(), LocalDate.now());
        totalRevenue = totalRevenue.multiply(BigDecimal.valueOf(AppConstants.SHARING_REVENUE));
        if (totalRevenue.compareTo(BigDecimal.valueOf(AppConstants.MINIMUM_WITHDRAW_AMOUNT)) < 0){
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.MINIMUM_AMOUNT,
                    AppConstants.MINIMUM_WITHDRAW_AMOUNT, totalRevenue);
        }
        return totalRevenue;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw() {
        User user = getOwner();
        BigDecimal amount = getAmountCanWithdraw(user.getUsername());
        OwnerWithdrawInfo ownerWithdrawInfo = ownerWithdrawInfoRepository.findByOwnerUsername(user.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("OwnerWithdrawInfo", "ownerUsername", user.getUsername()));
        ownerWithdrawInfo.setPendingLastWithdrawDate(LocalDate.now());
        ownerWithdrawInfoRepository.save(ownerWithdrawInfo);
        transactionRepository.save(Transaction.builder()
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .user(user)
                .status(TransactionStatus.PENDING)
                .type(TransactionType.WITHDRAWAL)
                .build());
        return true;
    }

    private User getOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(currentUsername, currentUsername).orElseThrow(
                ()-> new ResourceNotFoundException("User", "username", "owner"));
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_OWNER"))){
            throw new ResourceNotFoundException("User", "username", "owner");
        }
        return user;
    }
}
