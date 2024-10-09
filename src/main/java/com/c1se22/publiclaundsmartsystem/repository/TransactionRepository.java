package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
