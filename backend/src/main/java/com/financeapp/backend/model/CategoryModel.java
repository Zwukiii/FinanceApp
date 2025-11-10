package com.financeapp.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="category")
@Getter
@Setter

public class CategoryModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    @Column(nullable = false)
    private long id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
