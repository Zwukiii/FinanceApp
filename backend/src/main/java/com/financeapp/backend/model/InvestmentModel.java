package com.financeapp.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Investments")
public class InvestmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "You need to enter a ticket symbol!")
    @Column(nullable = false, length = 100)
    private String ticker;

    @DecimalMin(value = "0.0", message = "Amount invested needs to be greater than 0")
    @NotNull(message = "You need to enter amount invested!")
    private BigDecimal amountInvested;

    @NotNull(message = "You need to enter current value!")
    @Column(nullable = false)
    private BigDecimal currentValue;

    // Optional now
    private LocalDate investmentDate;
    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal roi;

    @NotBlank(message = "You need to enter category!")
    @Column(nullable = false, length = 100)
    private String category;

    @NotBlank(message = "You need to enter investment platform!")
    @Column(nullable = false, length = 100)
    private String platform;

    @Column(name = "date")
    private LocalDate date;

    @PrePersist
    public void prePersist() {
        if (investmentDate == null) investmentDate = LocalDate.now();
        if (startDate == null) startDate = LocalDate.now();
        if (endDate == null) endDate = LocalDate.now().plusMonths(1);
        if (date == null) date = LocalDate.now();
    }
}
