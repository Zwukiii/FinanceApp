package com.financeapp.backend.services;

import com.financeapp.backend.model.MonthlyHistoryModel;
import com.financeapp.backend.model.TransactionModel;
import com.financeapp.backend.repository.InvestmentRepository;
import com.financeapp.backend.repository.MonthlyHistoryRepository;
import com.financeapp.backend.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service

public class MonthlyHistoryService {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal totalInvestment;
    private BigDecimal balance;

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


    public BigDecimal calculateTotalIncome() {
        return transactionRepository.findAll().stream()
                .filter(t -> t.getType() != null && t.getType().toString().equalsIgnoreCase("INCOME"))
                .map(TransactionModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public BigDecimal calculateTotalExpenses() {
        return transactionRepository.findAll().stream()
                .filter(e -> e.getType() != null && e.getType().toString().equalsIgnoreCase("EXPENSE"))
                .map(TransactionModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public BigDecimal calculateTotalInvestments() {
        return transactionRepository.findAll().stream()
                .filter(i -> i.getType() != null && i.getType().toString().equalsIgnoreCase("INVESTMENT"))
                .map(TransactionModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }


    public BigDecimal calculateBalance(BigDecimal income, BigDecimal expenses, BigDecimal investments) {
        return check(income)
                .subtract(check(expenses))
                .subtract(check(investments));

    }

    private final BigDecimal check(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    @Async
    public void generateMonthlySummary() {
        BigDecimal income = calculateTotalIncome();
        BigDecimal expense = calculateTotalExpenses();
        BigDecimal investments = calculateTotalInvestments();
        BigDecimal balance = calculateBalance(income, expense, investments);

        MonthlyHistoryModel totalHistory = MonthlyHistoryModel.builder()
                .month(LocalDate.now().getMonth().toString())
                .totalIncome(income)
                .totalExpenses(expense)
                .totalInvestments(investments)
                .balance(balance)
                .build();

        monthlyHistoryRepository.save(totalHistory);
    }


}
