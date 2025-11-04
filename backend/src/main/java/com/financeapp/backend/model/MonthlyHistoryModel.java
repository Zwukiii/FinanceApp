package com.financeapp.backend.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monthlyhistory")
public class MonthlyHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal totalInvestments;
    private BigDecimal balance;
    private String transactionsJson;
}
