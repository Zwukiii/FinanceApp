package com.financeapp.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InvestmentRequestDTO {
    private String ticker;
    private BigDecimal amountInvested;
    private BigDecimal currentValue;
    private String category;
    private String platform;
    private LocalDate investmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
}
