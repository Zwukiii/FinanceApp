package com.financeapp.backend.exception;

import com.financeapp.backend.model.BudgetModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class BudgetValidator {

    public void validateForCreateBudget(BudgetModel budget) {

        if (budget == null) {
            throw new IllegalArgumentException("Budget cannot be null");
        }

        if (budget.getCategory() == null || budget.getCategory().isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        BigDecimal limitValue = budget.getLimitAmount();

        if (limitValue == null) {
            throw new IllegalArgumentException("Limit amount cannot be null");
        }

        if (limitValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Limit amount cannot be negative");
        }
    }
}
