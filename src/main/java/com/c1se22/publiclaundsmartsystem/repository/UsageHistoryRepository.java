package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.UsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Integer> {
}
