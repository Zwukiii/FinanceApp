package com.financeapp.backend.DTO.history;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class MonthlyHistoryResponseDTO {
    private Long id;
    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal totalInvestments;
    private BigDecimal balance;

}
