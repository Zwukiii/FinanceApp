package com.financeapp.backend.mappers;

import com.financeapp.backend.DTO.InvestmentRequestDTO;
import com.financeapp.backend.DTO.InvestmentResponseDTO;
import com.financeapp.backend.model.InvestmentModel;
import org.springframework.stereotype.Component;

@Component
public class InvestmentMapper {
    public InvestmentModel toEntity(InvestmentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        InvestmentModel invest = new InvestmentModel();

        invest.setTicker(dto.getTicker());
        invest.setAmountInvested(dto.getAmountInvested());
        invest.setCurrentValue(dto.getCurrentValue());
        invest.setInvestmentDate(dto.getInvestmentDate());
        invest.setCategory(dto.getCategory());
        invest.setPlatform(dto.getPlatform());
        invest.setStartDate(dto.getStartDate());
        invest.setEndDate(dto.getEndDate());
        return invest;

    }

    public InvestmentResponseDTO toDTO(InvestmentModel entity) {
        if (entity == null) {
            return null;
        }
        InvestmentResponseDTO responseDTO = new InvestmentResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setTicker(entity.getTicker());
        responseDTO.setAmountInvested(entity.getAmountInvested());
        responseDTO.setCurrentValue(entity.getCurrentValue());
        responseDTO.setRoi(entity.getRoi());
        responseDTO.setCategory(entity.getCategory());
        responseDTO.setPlatform(entity.getPlatform());
        responseDTO.setInvestmentDate(entity.getInvestmentDate());
        return responseDTO;
    }

    public void updateEntityFromDTO(InvestmentRequestDTO dto, InvestmentModel entity) {
        entity.setTicker(dto.getTicker());
        entity.setAmountInvested(dto.getAmountInvested());
        entity.setCurrentValue(dto.getCurrentValue());
        entity.setInvestmentDate(dto.getInvestmentDate());
        entity.setCategory(dto.getCategory());
        entity.setPlatform(dto.getPlatform());
    }
}
