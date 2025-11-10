package com.financeapp.backend.DTO.budget;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter

public class BudgetResponseDTO {
    private Long id;
    private String category;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private BigDecimal progress;
    private LocalDate startDate;
    private LocalDate endDate;
}
