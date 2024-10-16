package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.UsageHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Integer> {
    List<UsageHistory> findAllByStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT u.washingType.id, COUNT(u) FROM UsageHistory u WHERE u.startTime BETWEEN :start AND :end GROUP BY u.machine.id")
    List<Object[]> countByWashingTypeAndStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT u.washingType.id, SUM(u.cost) FROM UsageHistory u WHERE u.startTime BETWEEN :start AND :end GROUP BY u.machine.id")
    List<Object[]> sumCostByWashingTypeAndStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query(value = "SELECT u.user.id, COUNT(u) FROM UsageHistory u WHERE u.startTime BETWEEN :start AND :end GROUP BY u.user.id ORDER BY COUNT(u) DESC", nativeQuery = true)
    List<Object[]> findTopUsersByStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);
    @Query("SELECT u.user.id, COUNT(u) FROM UsageHistory u WHERE u.startTime BETWEEN :start AND :end GROUP BY u.user.id")
    List<Object[]> findUserUsageCountByStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT SUM(u.cost) FROM UsageHistory u WHERE u.startTime BETWEEN :start AND :end")
    BigDecimal sumCostByStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT COUNT(u) FROM UsageHistory u WHERE u.startTime BETWEEN :start AND :end")
    Long countByStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
