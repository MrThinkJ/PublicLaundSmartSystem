package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {
    TransactionService transactionService;
}
