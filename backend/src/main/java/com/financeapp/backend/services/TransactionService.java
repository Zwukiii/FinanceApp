package com.financeapp.backend.services;

import com.financeapp.backend.DTO.transaction.TransactionRequestDTO;
import com.financeapp.backend.mappers.TransactionMapper;
import com.financeapp.backend.model.TransactionModel;
import com.financeapp.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetService budgetService;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, BudgetService budgetService, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.budgetService = budgetService;
        this.transactionMapper = transactionMapper;
    }

    public TransactionModel addTransaction(TransactionModel transaction) {
        TransactionModel savedTransaction = transactionRepository.save(transaction);

        if ("EXPENSE".equalsIgnoreCase(String.valueOf(savedTransaction.getType())) ||
                "INVESTMENT".equalsIgnoreCase(String.valueOf(savedTransaction.getType()))) {
            budgetService.updateSpentAmount(
                    savedTransaction.getCategory(),
                    savedTransaction.getAmount()
            );
        }
        return savedTransaction;
    }

    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.findAll();
    }


    public Optional<TransactionModel> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public TransactionModel updateTransaction(Long id, TransactionRequestDTO dto) {
        TransactionModel update = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction doesn't exist by that id!"));
        transactionMapper.updateEntityFromDTO(dto, update);
        return transactionRepository.save(update);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);

    }


}
