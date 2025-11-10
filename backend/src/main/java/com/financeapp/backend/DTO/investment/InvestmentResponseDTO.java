package com.financeapp.backend.DTO.investment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter

public class InvestmentResponseDTO {
    private Long id;
    private String ticker;
    private BigDecimal amountInvested;
    private BigDecimal currentValue;
    private BigDecimal roi;
    private String category;
    private String platform;
    private LocalDate investmentDate;
}
