package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.UsageHistoryDto;
import com.c1se22.publiclaundsmartsystem.payload.UserUsageDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UsageHistoryService {
    List<UsageHistoryDto> getAllUsageHistories();
    UsageHistoryDto getUsageHistoryById(Integer id);
    void createUsageHistory(UsageHistoryDto usageHistoryDto);
    void completeUsageHistory(Integer id);
    void deleteUsageHistory(Integer id);
    List<UsageHistoryDto> getUsageHistoriesBetween(LocalDateTime start, LocalDateTime end);
    Map<String, Long> getUsageCountByWashingType(LocalDateTime start, LocalDateTime end);
    Map<String, BigDecimal> getRevenueByWashingType(LocalDateTime start, LocalDateTime end);
    List<UserUsageDto> getTopUsers(LocalDateTime start, LocalDateTime end, int limit);
    Map<String, Long> getUserUsageCount(LocalDateTime start, LocalDateTime end);
    BigDecimal getTotalRevenue(LocalDateTime start, LocalDateTime end);
    long getTotalUsageCount(LocalDateTime start, LocalDateTime end);
}
