package com.financeapp.backend.model;

import com.financeapp.backend.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class TransactionModel {

    public TransactionModel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", message = "Amount must be greater than 0")
    @NotNull(message = "You need to enter an amount!")
    private BigDecimal amount;

    @NotBlank(message = "You need to enter category!")
    @Column(nullable = false, length = 100)
    private String category;

    @NotBlank(message = "You need to enter category!")
    @Column(nullable = false, length = 100)
    private String description;


    @NotNull(message = "You need to enter date!")
    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "You need enter type")
    @Column(nullable = false, length = 15)
    private TransactionType type;




}
