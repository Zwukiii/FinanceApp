package com.financeapp.backend.mappers;

import com.financeapp.backend.DTO.budget.BudgetRequestDTO;
import com.financeapp.backend.DTO.budget.BudgetResponseDTO;
import com.financeapp.backend.model.BudgetModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BudgetMapper {

    public BudgetModel toEntity(BudgetRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        BudgetModel entity = new BudgetModel();

        entity.setCategory(dto.getCategory());
        entity.setLimitAmount(dto.getLimitAmount());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }

    public BudgetResponseDTO toDTO(BudgetModel entity) {
        if (entity == null) {
            return null;
        }

        BudgetResponseDTO dto = new BudgetResponseDTO();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setLimitAmount(entity.getLimitAmount());
        dto.setSpentAmount(entity.getSpentAmount());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());


        BigDecimal spentAmount = BigDecimal.ZERO;
        if (entity.getSpentAmount() != null) {
            spentAmount = entity.getSpentAmount();
        }

        BigDecimal limitAmount = BigDecimal.ZERO;
        if (entity.getLimitAmount() != null) {
            limitAmount = entity.getLimitAmount();
        }

        if (limitAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = spentAmount
                    .divide(limitAmount, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            if (progress.compareTo(BigDecimal.valueOf(100)) > 0) {
                progress = BigDecimal.valueOf(100);
            }
            dto.setProgress(progress);


        } else {
            dto.setProgress(BigDecimal.ZERO);
        }
        return dto;

    }


    public void updateEntityFromDTO(BudgetRequestDTO dto, BudgetModel entity) {
        entity.setCategory(dto.getCategory());
        entity.setLimitAmount(dto.getLimitAmount());

        if (dto.getSpentAmount() != null) {
            entity.setSpentAmount(dto.getSpentAmount());
        }
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
    }

}

