package com.financeapp.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "budgets")
public class BudgetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @NotBlank(message = "You need to enter category!")
    @Column(nullable = false, length = 100)
    private String category;

    @DecimalMin(value = "0.0", message = "Limit amount must be greater than 0")
    @NotNull(message = "You need to enter limit amount!")
    private BigDecimal limitAmount;


    @NotNull(message = "You need to enter spent amount!")
    @DecimalMin(value =  "0.0", message = "Amount needs to be greater than 0")
    @Column(nullable = false, length = 100)
    private BigDecimal spentAmount = BigDecimal.ZERO;


    @Column(nullable = false, length = 100)
    @NotNull(message = "You need to enter start Date!")
    private LocalDate startDate;


    @Column(nullable = false, length = 100)
    @NotNull(message = "You need to enter end Date!")
    private LocalDate endDate;
}
