package com.financeapp.backend.repository;

import com.financeapp.backend.model.MonthlyHistoryModel;
import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyHistoryRepository extends JpaRepository<MonthlyHistoryModel, Long> {

    List<MonthlyHistoryModel> findByUser(User user);
}
