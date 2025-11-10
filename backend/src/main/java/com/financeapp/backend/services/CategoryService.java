package com.financeapp.backend.services;

import com.financeapp.backend.DTO.CategoryRequestDTO;
import com.financeapp.backend.DTO.CategoryResponseDTO;
import com.financeapp.backend.mappers.CategoryMapper;
import com.financeapp.backend.model.CategoryModel;
import com.financeapp.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO input) {
        if (categoryRepository.existsByName(input.getName())) {
            throw new RuntimeException("Category already exists with that name!");
        }
        CategoryModel category = categoryMapper.dtoToEntity(input);
        CategoryModel saveToDto = categoryRepository.save(category);
        return CategoryMapper.entityToDto(saveToDto);

    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO input) {
        CategoryModel update = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category doesn't exists by that id!" + id)
        );
        categoryMapper.updateEntityFromDto(input, update);
        CategoryModel saveUpdate = categoryRepository.save(update);
        return CategoryMapper.entityToDto(saveUpdate);

    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw  new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
     }

    public List<CategoryResponseDTO> allCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::entityToDto)
                .toList();
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        CategoryModel category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return CategoryMapper.entityToDto(category);
    }




}
