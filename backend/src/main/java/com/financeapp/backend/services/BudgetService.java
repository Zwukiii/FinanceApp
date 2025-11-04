package com.financeapp.backend.services;

import com.financeapp.backend.DTO.BudgetRequestDTO;
import com.financeapp.backend.mappers.BudgetMapper;
import com.financeapp.backend.model.BudgetModel;
import com.financeapp.backend.repository.BudgetRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    public BudgetService(BudgetRepository budgetRepository, BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
    }

    public BudgetModel createBudget(BudgetModel budget) {
        budget.setSpentAmount(BigDecimal.ZERO);
        return budgetRepository.save(budget);
    }

    public List<BudgetModel> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Optional<BudgetModel> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    public BudgetModel updateBudget(Long id, BudgetRequestDTO dto) {
        BudgetModel updateBudget = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget doesn't exist by that id!"));
        budgetMapper.updateEntityFromDTO(dto, updateBudget);
        return budgetRepository.save(updateBudget);

    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    public void updateSpentAmount(String category, BigDecimal amount) {
        budgetRepository.findByCategory(category).ifPresent(budget -> {
            budget.setSpentAmount(budget.getSpentAmount().add(amount));
            budgetRepository.save(budget);
        });
    }

    public BigDecimal calculateProgress(Long id) {
        BudgetModel budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));

        BigDecimal spent = budget.getSpentAmount() == null ? BigDecimal.ZERO : budget.getSpentAmount();
        BigDecimal limit = budget.getLimitAmount() == null ? BigDecimal.ZERO : budget.getLimitAmount();


        if (limit.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return spent
                .divide(limit, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }



    public List<BudgetModel> getBudgetsOverLimit(String category, BigDecimal amount) {
        // Find all budgets
        List<BudgetModel> overLimitBudgets = budgetRepository.findAll()
                .stream()
                .filter(budget -> budget.getCategory().equals(category))  // Filter by category
                .peek(budget -> budget.setSpentAmount(budget.getSpentAmount().add(amount)))
                .filter(budget -> budget.getSpentAmount().compareTo(budget.getLimitAmount()) > 0)
                .collect(Collectors.toList());

        // If there are over-limit budgets, throw an exception
        if (!overLimitBudgets.isEmpty()) {
            throw new RuntimeException("Budget for category " + category + " has exceeded the limit.");
        }

        return overLimitBudgets;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetMonthlyBudgets() {
        List<BudgetModel> budgets = budgetRepository.findAll();

        for (var bt : budgets) {
            bt.setSpentAmount(BigDecimal.ZERO);

            budgetRepository.save(bt);
        }
        System.out.println("Monthly budgets have been reset");
    }

    public List<BudgetModel> getBudgetsOverLimit() {
        return budgetRepository.findBudgetsOverLimit();
    }
}
