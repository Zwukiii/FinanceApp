package com.financeapp.backend.repository;

import com.financeapp.backend.model.BudgetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BudgetRepository extends JpaRepository<BudgetModel, Long> {

    Optional<BudgetModel> findByCategory(String category);

    @Query("SELECT b FROM BudgetModel b WHERE b.spentAmount > b.limitAmount")
    List<BudgetModel> findBudgetsOverLimit();



}
