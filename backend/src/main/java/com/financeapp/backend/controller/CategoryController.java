package com.financeapp.backend.controller;

import com.financeapp.backend.DTO.category.CategoryRequestDTO;
import com.financeapp.backend.DTO.category.CategoryResponseDTO;
import com.financeapp.backend.mappers.CategoryMapper;
import com.financeapp.backend.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CategoryRequestDTO input) {
        return categoryService.createCategory(input);

    }
    @PutMapping("/{id}")
    public CategoryResponseDTO changeCategory(@PathVariable Long id,
                                              @RequestBody CategoryRequestDTO input) {
        return categoryService.updateCategory(id, input);

    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategoriesSaved() {
        return categoryService.allCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO SpecificCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

}

