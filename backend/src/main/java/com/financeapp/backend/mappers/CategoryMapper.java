package com.financeapp.backend.mappers;

import com.financeapp.backend.DTO.CategoryRequestDTO;
import com.financeapp.backend.DTO.CategoryResponseDTO;
import com.financeapp.backend.model.CategoryModel;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryModel dtoToEntity(CategoryRequestDTO input) {
        if (input == null) {
            return null;
        }
        CategoryModel category = new CategoryModel();

        category.setName(input.getName());
        category.setType(input.getType());


        return category;
    }

    public static CategoryResponseDTO entityToDto(CategoryModel entity) {
        if (entity == null) {
            return null;
        }
        CategoryResponseDTO resp = new CategoryResponseDTO();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setType(entity.getType());
        return resp;

    }

    public void updateEntityFromDto(CategoryRequestDTO dto, CategoryModel update) {
        category.setName(dto.getName());
        category.setType(dto.getType());

    }
}
