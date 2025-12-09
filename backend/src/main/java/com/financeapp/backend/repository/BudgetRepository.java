package com.financeapp.backend.repository;

import com.financeapp.backend.model.BudgetModel;
import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BudgetRepository extends JpaRepository<BudgetModel, Long> {

    List<BudgetModel> findByUser(User user);

    Optional<BudgetModel> findByIdAndUser(Long id, User user);

    Optional<BudgetModel> findByCategoryAndUser(String category, User user);

    @Query("SELECT b FROM BudgetModel b WHERE b.spentAmount > b.limitAmount AND b.user = :user")
    List<BudgetModel> findBudgetsOverLimitByUser(@Param("user") User user);

    void deleteByIdAndUser(Long id, User user);
}
