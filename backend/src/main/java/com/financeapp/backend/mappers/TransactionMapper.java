package com.financeapp.backend.mappers;

import com.financeapp.backend.DTO.transaction.TransactionRequestDTO;
import com.financeapp.backend.DTO.transaction.TransactionResponseDTO;
import com.financeapp.backend.model.TransactionModel;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionModel toEntity(TransactionRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        TransactionModel entity = new TransactionModel();
        entity.setAmount(dto.getAmount());
        entity.setCategory(dto.getCategory());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setType(dto.getType());
        return entity;
    }

    public TransactionResponseDTO toDTO(TransactionModel entity) {
        if (entity == null) {
            return null;
        }

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setAmount(entity.getAmount());
        responseDTO.setCategory(entity.getCategory());
        responseDTO.setDescription(entity.getDescription());
        responseDTO.setDate(entity.getDate());
        responseDTO.setType(entity.getType());
        return responseDTO;
    }

    public void updateEntityFromDTO(TransactionRequestDTO dto, TransactionModel entity) {
        dto.setAmount(entity.getAmount());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setDate(entity.getDate());
        dto.setType(entity.getType());

    }
}
