package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.request.OwnerWithdrawInfoRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OwnerService {
    void updateUserToOwner(String username);
    BigDecimal getTotalRevenue();
    BigDecimal getRevenueBeforeDate(LocalDate date);
    BigDecimal getRevenueByMonth(int month);
    BigDecimal getRevenueByYear(int year);
    BigDecimal getRevenueByMonthAndYear(int month, int year);
    BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate);
    Integer getNumberOfUsingByMonth(int month);
    Integer getNumberOfUsingByYear(int year);
    Boolean updateWithdrawInfo(OwnerWithdrawInfoRequestDto requestDto);
    Boolean withdraw();
}
