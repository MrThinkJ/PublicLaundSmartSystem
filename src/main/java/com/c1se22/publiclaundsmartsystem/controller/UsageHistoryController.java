package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.UsageHistoryDto;
import com.c1se22.publiclaundsmartsystem.payload.UserUsageDto;
import com.c1se22.publiclaundsmartsystem.service.UsageHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usage-history")
@AllArgsConstructor
public class UsageHistoryController {
    UsageHistoryService usageHistoryService;
    @GetMapping
    public ResponseEntity<List<UsageHistoryDto>> getAllUsageHistories() {
        return ResponseEntity.ok(usageHistoryService.getAllUsageHistories());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UsageHistoryDto> getUsageHistoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(usageHistoryService.getUsageHistoryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsageHistory(@PathVariable Integer id) {
        usageHistoryService.deleteUsageHistory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/between")
    public ResponseEntity<List<UsageHistoryDto>> getUsageHistoriesBetween(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getUsageHistoriesBetween(startDate, endDate));
    }

    @GetMapping("/count/washing-type")
    public ResponseEntity<Map<String, Long>> getUsageCountByWashingType(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getUsageCountByWashingType(startDate, endDate));
    }

    @GetMapping("/revenue/washing-type")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueByWashingType(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getRevenueByWashingType(startDate, endDate));
    }

    @GetMapping("/top-users")
    public ResponseEntity<List<UserUsageDto>> getTopUsers(@RequestParam String start, @RequestParam String end, @RequestParam int limit) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getTopUsers(startDate, endDate, limit));
    }

    @GetMapping("/user-usage-count")
    public ResponseEntity<Map<String, Long>> getUserUsageCount(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getUserUsageCount(startDate, endDate));
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getTotalRevenue(startDate, endDate));
    }

    @GetMapping("/total-usage-count")
    public ResponseEntity<Long> getTotalUsageCount(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(usageHistoryService.getTotalUsageCount(startDate, endDate));
    }
}
