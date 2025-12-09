package com.financeapp.backend.controller;

import com.financeapp.backend.DTO.transaction.TransactionRequestDTO;
import com.financeapp.backend.DTO.transaction.TransactionResponseDTO;
import com.financeapp.backend.mappers.TransactionMapper;
import com.financeapp.backend.model.TransactionModel;
import com.financeapp.backend.services.MonthlyHistoryService;
import com.financeapp.backend.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final MonthlyHistoryService monthlyHistoryService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, MonthlyHistoryService monthlyHistoryService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.monthlyHistoryService = monthlyHistoryService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping
    public TransactionResponseDTO addTransaction(@RequestBody TransactionRequestDTO dto) {
        TransactionModel entity = transactionMapper.toEntity(dto);
        TransactionModel saved = transactionService.addTransaction(entity);
        return transactionMapper.toDTO(saved);
    }

    @GetMapping
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(@PathVariable Long id, @RequestBody TransactionRequestDTO dto) {
        TransactionModel updated = transactionService.updateTransaction(id, dto);
        monthlyHistoryService.generateMonthlySummary();
        return transactionMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable Long id) {
        TransactionModel transaction = transactionService.getTransactionById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found!"));

        return transactionMapper.toDTO(transaction);
    }


}
