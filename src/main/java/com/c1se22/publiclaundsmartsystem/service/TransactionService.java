package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.enums.TransactionStatus;
import com.c1se22.publiclaundsmartsystem.payload.TransactionDto;

import java.util.List;

public interface TransactionService {
  List<TransactionDto> getAllTransactions();
  TransactionDto getTransactionById(Integer id);
  TransactionDto updateTransaction(Integer id, TransactionDto transactionDto);
  void deleteTransaction(Integer id);
  List<TransactionDto> getTransactionsByUserId(Integer userId);
  List<TransactionDto> getTransactionsByUsername(String username);
  List<TransactionDto> getTransactionsByStatus(TransactionStatus status);
}
