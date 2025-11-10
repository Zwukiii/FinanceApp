package com.financeapp.backend.DTO.category;

import com.financeapp.backend.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private long id;
    private String name;
    private TransactionType type;
}
