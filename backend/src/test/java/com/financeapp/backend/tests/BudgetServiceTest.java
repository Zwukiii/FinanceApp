package com.financeapp.backend.tests;


import com.financeapp.backend.DTO.budget.BudgetRequestDTO;
import com.financeapp.backend.DTO.budget.BudgetResponseDTO;
import com.financeapp.backend.exception.BudgetValidator;
import com.financeapp.backend.mappers.BudgetMapper;
import com.financeapp.backend.model.BudgetModel;
import com.financeapp.backend.repository.BudgetRepository;
import com.financeapp.backend.services.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
    private BudgetRepository repo;
    private BudgetMapper mapper;
    private BudgetValidator validator;
    private BudgetService service;

    private BudgetRequestDTO dto;
    private BudgetModel mappedEntity;
    private BudgetModel savedEntity;
    private BudgetResponseDTO responseDTO;


    @BeforeEach
    void setupSetters() {
        repo = Mockito.mock(BudgetRepository.class);
        mapper = Mockito.mock(BudgetMapper.class);
        validator = Mockito.mock(BudgetValidator.class);

        service = new BudgetService(repo, mapper, validator);

        dto = new BudgetRequestDTO();
        dto.setCategory("Food");
        dto.setLimitAmount(BigDecimal.valueOf(100));

        mappedEntity = new BudgetModel();
        mappedEntity.setCategory("Food");
        mappedEntity.setLimitAmount(BigDecimal.valueOf(100));

        savedEntity = new BudgetModel();
        savedEntity.setCategory("Food");
        savedEntity.setLimitAmount(BigDecimal.valueOf(100));
        savedEntity.setSpentAmount(BigDecimal.ZERO);

        responseDTO = new BudgetResponseDTO();


    }

    @Test
    void createBudget_shouldResetSpentAmountToZero() {
        when(mapper.toEntity(dto)).thenReturn(mappedEntity);
        when(repo.save(mappedEntity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(responseDTO);

        service.createBudget(dto);

        assertEquals(BigDecimal.ZERO, mappedEntity.getSpentAmount());
    }

    @Test
    void createBudget_shouldNotChangeLimitAmount() {
        dto.setLimitAmount(BigDecimal.valueOf(150));
        mappedEntity.setLimitAmount(BigDecimal.valueOf(150));
        savedEntity.setLimitAmount(BigDecimal.valueOf(150));

        when(mapper.toEntity(dto)).thenReturn(mappedEntity);
        when(repo.save(mappedEntity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(new BudgetResponseDTO());

        service.createBudget(dto);

        assertEquals(BigDecimal.valueOf(150), mappedEntity.getLimitAmount());
    }

    @Test
    void createBudget_ShouldThrowOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> service.createBudget(null));
    }

    @Test
    void createBudget_ShouldThrowWhenLimitIsNegative() {
        dto.setLimitAmount(BigDecimal.valueOf(-20));
        mappedEntity.setLimitAmount(BigDecimal.valueOf(-20));

        when(mapper.toEntity(dto)).thenReturn(mappedEntity);
        doThrow(new IllegalArgumentException())
                .when(validator).validateForCreateBudget(mappedEntity);

        assertThrows(IllegalArgumentException.class, () -> service.createBudget(dto));
    }

    @Test
    void createBudget_ShouldThrowWhenCategoryIsNull() {
        dto.setCategory(null);
        mappedEntity.setCategory(null);

        when(mapper.toEntity(dto)).thenReturn(mappedEntity);
        doThrow(new IllegalArgumentException())
                .when(validator).validateForCreateBudget(mappedEntity);

        assertThrows(IllegalArgumentException.class, () -> service.createBudget(dto));
    }


    @Test
    void createBudget_ShouldThrowWhenCategoryIsBlank() {
        dto.setCategory("");
        mappedEntity.setCategory("");

        when(mapper.toEntity(dto)).thenReturn(mappedEntity);
        doThrow(new IllegalArgumentException())
                .when(validator).validateForCreateBudget(mappedEntity);

        assertThrows(IllegalArgumentException.class, () -> service.createBudget(dto));
    }


    @Test
    void createBudget_ShouldCallMapper() {
        when(mapper.toEntity(dto)).thenReturn(mappedEntity);
        when(repo.save(mappedEntity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(new BudgetResponseDTO());
        service.createBudget(dto);

        verify(mapper).toEntity(dto);
    }

    @Test
    void CalculateProgress_ShouldThrowWhenBudgetNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.calculateProgress(1L));

    }

    @Test
    void calculateProgress_ShouldReturnZeroWhenLimitIsZeroOrNull() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.valueOf(150));
        budget.setLimitAmount(null);

        when(repo.findById(1L)).thenReturn(Optional.of(budget));
        BigDecimal result1 = service.calculateProgress(1L);

        assertEquals(BigDecimal.ZERO, result1);
        budget.setLimitAmount(BigDecimal.ZERO);
        when(repo.findById(2L)).thenReturn(Optional.of(budget));

        BigDecimal result2 = service.calculateProgress(2L);
        assertEquals(BigDecimal.ZERO, result2);
    }

    @Test
    void calculateProgress_ShouldReturnCorrectPercentage() {
        //Arrange
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.valueOf(50));
        budget.setLimitAmount(BigDecimal.valueOf(200));

        //Act
        when(repo.findById(15L)).thenReturn(Optional.of(budget));
        BigDecimal result = service.calculateProgress(15L);

        //Assert
        assertEquals(new BigDecimal("25.00"), result);
    }

    @Test
    void updateBudget_ShouldUpdateAndSave() {
        Long id = 1L;

        BudgetRequestDTO dto = new BudgetRequestDTO();
        dto.setCategory("Updated");
        dto.setLimitAmount(BigDecimal.valueOf(200));

        BudgetModel existing = new BudgetModel();
        existing.setId(id);
        existing.setCategory("Old");
        existing.setLimitAmount(BigDecimal.valueOf(100));

        BudgetModel saved = new BudgetModel();
        saved.setId(id);
        saved.setCategory("Updated");
        saved.setLimitAmount(BigDecimal.valueOf(200));

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(saved);

        BudgetModel result = service.updateBudget(id, dto);

        verify(repo).findById(id);
        verify(mapper).updateEntityFromDTO(dto, existing);
        verify(repo).save(existing);

        assertEquals("Updated", result.getCategory());
        assertEquals(BigDecimal.valueOf(200), result.getLimitAmount());
    }

    @Test
    void updateBudget_ShouldThrowWhenIdNotFound() {
        Long id = 99L;

        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateBudget(id, new BudgetRequestDTO()));
    }

    @Test
    void updateBudget_ShouldCallMapper() {
        Long id = 1L;

        BudgetRequestDTO dto = new BudgetRequestDTO();
        BudgetModel existing = new BudgetModel();

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        service.updateBudget(id, dto);

        verify(mapper).updateEntityFromDTO(dto, existing);
    }

    @Test
    void updateBudget_ShouldSaveUpdatedEntity() {
        Long id = 1L;

        BudgetRequestDTO dto = new BudgetRequestDTO();
        BudgetModel existing = new BudgetModel();

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        service.updateBudget(id, dto);
    }

    @Test
    void deleteBudget_ShouldNotCallOtherRepositortyMethods() {
        Long id = 1L;

        service.deleteBudget(id);
        verify(repo).deleteById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void updateSpentAmount_ShouldIncreaseSpentAmount() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(20);

        BudgetModel budget = new BudgetModel();
        budget.setCategory(category);
        budget.setSpentAmount(BigDecimal.valueOf(30));

        when(repo.findByCategory(category)).thenReturn(Optional.of(budget));

        service.updateSpentAmount(category, amount);

        assertEquals(BigDecimal.valueOf(50), budget.getSpentAmount());
        verify(repo).save(budget);
    }

    @Test
    void updateSpentAmount_ShouldDoNothingWhenCategoryNotFound() {
        String category = "Unknown";

        when(repo.findByCategory(category)).thenReturn(Optional.empty());

        service.updateSpentAmount(category, BigDecimal.TEN);

        verify(repo, never()).save(any());
    }

    @Test
    void updateSpentAmount_ShouldCallFindByCategory() {
        String category = "Food";

        when(repo.findByCategory(category)).thenReturn(Optional.empty());

        service.updateSpentAmount(category, BigDecimal.ONE);

        verify(repo).findByCategory(category);
    }

    @Test
    void calculateProgress_ShouldThrowWhenIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.calculateProgress(1L));
    }

    @Test
    void calculateProgress_ShouldReturnZeroWhenLimitIsZero() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.valueOf(50));
        budget.setLimitAmount(BigDecimal.ZERO);

        when(repo.findById(1L)).thenReturn(Optional.of(budget));

        BigDecimal result = service.calculateProgress(1L);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateProgress_ShouldReturnZeroWhenLimitIsNull() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.valueOf(50));
        budget.setLimitAmount(null);

        when(repo.findById(1L)).thenReturn(Optional.of(budget));

        BigDecimal result = service.calculateProgress(1L);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateProgress_ShouldReturnCorrectPercentage_WhenValuesAreValid() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.valueOf(50));
        budget.setLimitAmount(BigDecimal.valueOf(200));

        when(repo.findById(1L)).thenReturn(Optional.of(budget));

        BigDecimal result = service.calculateProgress(1L);

        assertEquals(BigDecimal.valueOf(25.00).setScale(2), result);
    }

    @Test
    void calculateProgress_ShouldTreatNullSpentAsZero() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(null);
        budget.setLimitAmount(BigDecimal.valueOf(100));

        when(repo.findById(1L)).thenReturn(Optional.of(budget));

        BigDecimal result = service.calculateProgress(1L);

        assertEquals(BigDecimal.ZERO.setScale(2), result);
    }

    @Test
    void calculateProgress_ShouldCallFindById() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.TEN);
        budget.setLimitAmount(BigDecimal.TEN);

        when(repo.findById(1L)).thenReturn(Optional.of(budget));

        service.calculateProgress(1L);

        verify(repo).findById(1L);
    }

    @Test
    void updateSpentAmount_ShouldAddAmount_WhenBudgetExists() {
        BudgetModel budget = new BudgetModel();
        budget.setSpentAmount(BigDecimal.valueOf(50));

        when(repo.findByCategory("Food")).thenReturn(Optional.of(budget));

        service.updateSpentAmount("Food", BigDecimal.valueOf(20));

        assertEquals(BigDecimal.valueOf(70), budget.getSpentAmount());
        verify(repo).save(budget);
    }

    @Test
    void updateSpentAmount_ShouldDoNothing_WhenBudgetDoesNotExist() {
        when(repo.findByCategory("Unknown")).thenReturn(Optional.empty());

        service.updateSpentAmount("Unknown", BigDecimal.valueOf(10));

        verify(repo, never()).save(any());
    }


    @Test
    void getBudgetsOverLimit_ShouldReturnEmptyList_WhenNoBudgetExceedsLimit() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(20);

        BudgetModel b1 = new BudgetModel();
        b1.setCategory("Food");
        b1.setSpentAmount(BigDecimal.valueOf(30));
        b1.setLimitAmount(BigDecimal.valueOf(100));

        BudgetModel b2 = new BudgetModel();
        b2.setCategory("Food");
        b2.setSpentAmount(BigDecimal.valueOf(10));
        b2.setLimitAmount(BigDecimal.valueOf(50));

        when(repo.findAll()).thenReturn(java.util.List.of(b1, b2));

        var result = service.getBudgetsOverLimit(category, amount);

        assertEquals(0, result.size());
    }

    @Test
    void getBudgetsOverLimit_ShouldThrowWhenBudgetExceedsLimitAfterAddingAmount() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(50);
        BudgetModel budget = new BudgetModel();
        budget.setCategory(category);
        budget.setSpentAmount(BigDecimal.valueOf(80));
        budget.setLimitAmount(BigDecimal.valueOf(100));
        when(repo.findAll()).thenReturn(List.of(budget));
        assertThrows(RuntimeException.class, () -> service.getBudgetsOverLimit(category, amount));


    }

    @Test
    void getBudgetsOverLimit_ShouldNotThrowWhenSpentEqualsLimit() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(20);

        BudgetModel budget = new BudgetModel();
        budget.setCategory(category);
        budget.setSpentAmount(BigDecimal.valueOf(80));
        budget.setLimitAmount(BigDecimal.valueOf(100));
        when(repo.findAll()).thenReturn(List.of(budget));
        assertDoesNotThrow(() -> service.getBudgetsOverLimit(category, amount));
    }

    @Test
    void getBudgetsOverLimit_ShouldThrowWhenAnyBudgetInCategoryIsOver() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(50);

        BudgetModel b1 = new BudgetModel();
        b1.setCategory(category);
        b1.setSpentAmount(BigDecimal.valueOf(60));
        b1.setLimitAmount(BigDecimal.valueOf(100));
        BudgetModel b2 = new BudgetModel();
        b2.setCategory(category);
        b2.setSpentAmount(BigDecimal.valueOf(10));
        b2.setLimitAmount(BigDecimal.valueOf(200));
        when(repo.findAll()).thenReturn(List.of(b1, b2));

        assertThrows(RuntimeException.class, () -> service.getBudgetsOverLimit(category, amount));
    }

    @Test
    void getBudgetsOverLimit_ShouldThrowWhenSpentAmountIsNull() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(20);

        BudgetModel b1 = new BudgetModel();
        b1.setCategory(category);
        b1.setSpentAmount(null);
        b1.setLimitAmount(BigDecimal.valueOf(100));

        when(repo.findAll()).thenReturn(List.of(b1));

        assertThrows(NullPointerException.class,
                () -> service.getBudgetsOverLimit(category, amount));
    }

    @Test
    void getBudgetsOverLimit_ShouldNotModifyOtherCategories() {
        String category = "Food";
        BigDecimal amount = BigDecimal.valueOf(60);

        BudgetModel food = new BudgetModel();
        food.setCategory("Food");
        food.setSpentAmount(BigDecimal.valueOf(50));
        food.setLimitAmount(BigDecimal.valueOf(100));

        BudgetModel travel = new BudgetModel();
        travel.setCategory("Travel");
        travel.setSpentAmount(BigDecimal.valueOf(10));
        travel.setLimitAmount(BigDecimal.valueOf(200));

        when(repo.findAll()).thenReturn(List.of(food, travel));

        assertThrows(RuntimeException.class,
                () -> service.getBudgetsOverLimit(category, amount));

        assertEquals(BigDecimal.valueOf(10), travel.getSpentAmount());
    }


}
