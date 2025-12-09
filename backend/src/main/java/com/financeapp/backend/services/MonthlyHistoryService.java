package com.financeapp.backend.services;

import com.financeapp.backend.model.MonthlyHistoryModel;
import com.financeapp.backend.model.TransactionModel;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repository.InvestmentRepository;
import com.financeapp.backend.repository.MonthlyHistoryRepository;
import com.financeapp.backend.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MonthlyHistoryService {

    private final TransactionRepository transactionRepository;
    private final InvestmentRepository investmentRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;

    public MonthlyHistoryService(TransactionRepository transactionRepository,
                                 InvestmentRepository investmentRepository,
                                 MonthlyHistoryRepository monthlyHistoryRepository) {
        this.transactionRepository = transactionRepository;
        this.investmentRepository = investmentRepository;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public BigDecimal calculateTotalIncome() {
        return transactionRepository.findByUser(getCurrentUser()).stream()
                .filter(t -> t.getType() != null && t.getType().toString().equalsIgnoreCase("INCOME"))
                .map(TransactionModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalExpenses() {
        return transactionRepository.findByUser(getCurrentUser()).stream()
                .filter(e -> e.getType() != null && e.getType().toString().equalsIgnoreCase("EXPENSE"))
                .map(TransactionModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalInvestments() {
        return transactionRepository.findByUser(getCurrentUser()).stream()
                .filter(i -> i.getType() != null && i.getType().toString().equalsIgnoreCase("INVESTMENT"))
                .map(TransactionModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateBalance(BigDecimal income, BigDecimal expenses, BigDecimal investments) {
        return check(income)
                .subtract(check(expenses))
                .subtract(check(investments));
    }

    private BigDecimal check(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    public List<MonthlyHistoryModel> getHistoryForCurrentUser() {
        return monthlyHistoryRepository.findByUser(getCurrentUser());
    }

    @Transactional
    public void generateMonthlySummary() {
        User currentUser = getCurrentUser();
        BigDecimal income = calculateTotalIncome();
        BigDecimal expense = calculateTotalExpenses();
        BigDecimal investments = calculateTotalInvestments();
        BigDecimal balance = calculateBalance(income, expense, investments);

        MonthlyHistoryModel totalHistory = MonthlyHistoryModel.builder()
                .user(currentUser)
                .month(LocalDate.now().getMonth().toString())
                .totalIncome(income)
                .totalExpenses(expense)
                .totalInvestments(investments)
                .balance(balance)
                .build();

        monthlyHistoryRepository.save(totalHistory);
    }
}
