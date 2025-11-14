package com.financeapp.backend.controller;


import com.financeapp.backend.DTO.budget.BudgetRequestDTO;
import com.financeapp.backend.DTO.budget.BudgetResponseDTO;
import com.financeapp.backend.mappers.BudgetMapper;
import com.financeapp.backend.model.BudgetModel;
import com.financeapp.backend.services.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetMapper budgetMapper;

    public BudgetController(BudgetService budgetService, BudgetMapper budgetMapper) {
        this.budgetService = budgetService;
        this.budgetMapper = budgetMapper;
    }

    @PostMapping
    public  BudgetResponseDTO createBudget(@RequestBody BudgetRequestDTO dto) {
        return budgetService.createBudget(dto);
    }

    @GetMapping
    public List<BudgetResponseDTO> getAllBudgets() {
        return budgetService.getAllBudgets()
                .stream()
                .map(budgetMapper::toDTO)
                .toList();

    }

    @GetMapping("/{id}")
    public BudgetResponseDTO getBudgetById(@PathVariable Long id) {
        BudgetModel budget = budgetService.getBudgetById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found!"));

        return budgetMapper.toDTO(budget);

    }

    @PutMapping("/{id}")
    public BudgetResponseDTO updateBudget(@PathVariable Long id, @RequestBody BudgetRequestDTO dto) {
        BudgetModel updated = budgetService.updateBudget(id, dto);
        return budgetMapper.toDTO(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok("Budget deleted successfully");
    }

    @GetMapping("/{id}/progress")
    public BigDecimal calculateProgress(@PathVariable Long id) {
        return budgetService.calculateProgress(id);
    }

    @GetMapping("/over-limit")
    public List<BudgetResponseDTO> getBudgetsOverLimit() {
        return budgetService.getBudgetsOverLimit()
                .stream()
                .map(budgetMapper::toDTO)
                .toList();
    }


}
