package com.c1se22.publiclaundsmartsystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OwnerService {
    void updateUserToOwner(String username);
    BigDecimal getTotalRevenue();
    BigDecimal getRevenueByMonth(int month);
    BigDecimal getRevenueByYear(int year);
    BigDecimal getRevenueByMonthAndYear(int month, int year);
    BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate);
    Integer getNumberOfUsingByMonth(int month);
    Integer getNumberOfUsingByYear(int year);
}
