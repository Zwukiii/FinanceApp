package com.financeapp.backend.services;

import com.financeapp.backend.DTO.budget.BudgetRequestDTO;
import com.financeapp.backend.DTO.budget.BudgetResponseDTO;
import com.financeapp.backend.exception.BudgetValidator;
import com.financeapp.backend.mappers.BudgetMapper;
import com.financeapp.backend.model.BudgetModel;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repository.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetService.class);

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetValidator budgetValidator;

    public BudgetService(BudgetRepository budgetRepository, BudgetMapper budgetMapper, BudgetValidator budgetValidator) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
        this.budgetValidator = budgetValidator;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public BudgetResponseDTO createBudget(BudgetRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("dto request cannot be null");
        }

        BudgetModel entity = budgetMapper.toEntity(dto);
        entity.setUser(getCurrentUser());
        entity.setSpentAmount(BigDecimal.ZERO);
        budgetValidator.validateForCreateBudget(entity);
        BudgetModel saveModel = budgetRepository.save(entity);
        return budgetMapper.toDTO(saveModel);
    }

    public List<BudgetModel> getAllBudgets() {
        return budgetRepository.findByUser(getCurrentUser());
    }

    public Optional<BudgetModel> getBudgetById(Long id) {
        return budgetRepository.findByIdAndUser(id, getCurrentUser());
    }

    @Transactional
    public BudgetModel updateBudget(Long id, BudgetRequestDTO dto) {
        User currentUser = getCurrentUser();
        BudgetModel updateBudget = budgetRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Budget doesn't exist by that id!"));
        budgetMapper.updateEntityFromDTO(dto, updateBudget);
        return budgetRepository.save(updateBudget);
    }

    @Transactional
    public void deleteBudget(Long id) {
        User currentUser = getCurrentUser();
        budgetRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Budget doesn't exist by that id!"));
        budgetRepository.deleteByIdAndUser(id, currentUser);
    }

    @Transactional
    public void updateSpentAmount(String category, BigDecimal amount, User user) {
        budgetRepository.findByCategoryAndUser(category, user).ifPresent(budget -> {
            budget.setSpentAmount(budget.getSpentAmount().add(amount));
            budgetRepository.save(budget);
        });
    }

    public BigDecimal calculateProgress(Long id) {
        BudgetModel budget = budgetRepository.findByIdAndUser(id, getCurrentUser())
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
        User currentUser = getCurrentUser();
        List<BudgetModel> overLimitBudgets = budgetRepository.findByUser(currentUser)
                .stream()
                .filter(budget -> budget.getCategory().equals(category))
                .peek(budget -> budget.setSpentAmount(budget.getSpentAmount().add(amount)))
                .filter(budget -> budget.getSpentAmount().compareTo(budget.getLimitAmount()) > 0)
                .collect(Collectors.toList());

        if (!overLimitBudgets.isEmpty()) {
            throw new RuntimeException("Budget for category " + category + " has exceeded the limit.");
        }

        return overLimitBudgets;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void resetMonthlyBudgets() {
        List<BudgetModel> budgets = budgetRepository.findAll();

        for (var bt : budgets) {
            bt.setSpentAmount(BigDecimal.ZERO);
            budgetRepository.save(bt);
        }
        logger.info("Monthly budgets have been reset");
    }

    public List<BudgetModel> getBudgetsOverLimit() {
        return budgetRepository.findBudgetsOverLimitByUser(getCurrentUser());
    }
}
