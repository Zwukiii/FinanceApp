package com.financeapp.backend.tests;

import com.financeapp.backend.exception.BudgetValidator;
import com.financeapp.backend.model.BudgetModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BudgetValidatorTest {

    private BudgetValidator validator;

    @BeforeEach
    void setup() {
        validator = new BudgetValidator();
    }

    @Test
    void validateForCreateBudget_ShouldThrowWhenBudgetIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateForCreateBudget(null));
    }

    @Test
    void validateForCreateBudget_ShouldThrowWhenCategoryIsNull() {
        BudgetModel budget = new BudgetModel();
        budget.setCategory(null);
        budget.setLimitAmount(BigDecimal.TEN);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateForCreateBudget(budget));
    }

    @Test
    void validateForCreateBudget_ShouldThrowWhenCategoryIsBlank() {
        BudgetModel budget = new BudgetModel();
        budget.setCategory("");
        budget.setLimitAmount(BigDecimal.TEN);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateForCreateBudget(budget));
    }

    @Test
    void validateForCreateBudget_ShouldThrowWhenLimitIsNull() {
        BudgetModel budget = new BudgetModel();
        budget.setCategory("Food");
        budget.setLimitAmount(null);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateForCreateBudget(budget));
    }

    @Test
    void validateForCreateBudget_ShouldThrowWhenLimitIsNegative() {
        BudgetModel budget = new BudgetModel();
        budget.setCategory("Food");
        budget.setLimitAmount(BigDecimal.valueOf(-5));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateForCreateBudget(budget));
    }

    @Test
    void validateForCreateBudget_ShouldNotThrowWhenValid() {
        BudgetModel budget = new BudgetModel();
        budget.setCategory("Food");
        budget.setLimitAmount(BigDecimal.valueOf(100));

        assertDoesNotThrow(() -> validator.validateForCreateBudget(budget));
    }
}
