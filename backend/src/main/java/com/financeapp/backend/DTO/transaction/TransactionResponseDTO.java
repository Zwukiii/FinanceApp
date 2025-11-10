package com.financeapp.backend.DTO.transaction;

import com.financeapp.backend.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate date;
    private TransactionType type;
}
