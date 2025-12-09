package com.financeapp.backend.services;

import com.financeapp.backend.DTO.transaction.TransactionRequestDTO;
import com.financeapp.backend.mappers.TransactionMapper;
import com.financeapp.backend.model.TransactionModel;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public TransactionModel addTransaction(TransactionModel transaction) {
        User currentUser = getCurrentUser();
        transaction.setUser(currentUser);
        TransactionModel savedTransaction = transactionRepository.save(transaction);

        if ("EXPENSE".equalsIgnoreCase(String.valueOf(savedTransaction.getType())) ||
                "INVESTMENT".equalsIgnoreCase(String.valueOf(savedTransaction.getType()))) {
            budgetService.updateSpentAmount(
                    savedTransaction.getCategory(),
                    savedTransaction.getAmount(),
                    currentUser
            );
        }
        return savedTransaction;
    }

    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.findByUser(getCurrentUser());
    }

    public Optional<TransactionModel> getTransactionById(Long id) {
        return transactionRepository.findByIdAndUser(id, getCurrentUser());
    }

    @Transactional
    public TransactionModel updateTransaction(Long id, TransactionRequestDTO dto) {
        User currentUser = getCurrentUser();
        TransactionModel update = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Transaction doesn't exist by that id!"));
        transactionMapper.updateEntityFromDTO(dto, update);
        return transactionRepository.save(update);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        User currentUser = getCurrentUser();
        transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Transaction doesn't exist by that id!"));
        transactionRepository.deleteByIdAndUser(id, currentUser);
    }
}
