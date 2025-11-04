package com.financeapp.backend.mappers;

import com.financeapp.backend.DTO.MonthlyHistoryResponseDTO;
import com.financeapp.backend.model.MonthlyHistoryModel;
import org.springframework.stereotype.Component;

@Component
public class MonthlyHistoryMapper {

    public static MonthlyHistoryResponseDTO toDTO(MonthlyHistoryModel entity) {
        if (entity == null) {
            return null;
        }

        MonthlyHistoryResponseDTO dto = new MonthlyHistoryResponseDTO();
        dto.setId(entity.getId());
        dto.setMonth(entity.getMonth());
        dto.setTotalIncome(entity.getTotalIncome());
        dto.setTotalExpenses(entity.getTotalExpenses());
        dto.setTotalInvestments(entity.getTotalInvestments());
        dto.setBalance(entity.getBalance());
        return dto;
    }


}
