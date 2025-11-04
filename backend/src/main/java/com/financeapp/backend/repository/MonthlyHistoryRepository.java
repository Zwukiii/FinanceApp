package com.financeapp.backend.repository;

import com.financeapp.backend.model.MonthlyHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyHistoryRepository extends JpaRepository<MonthlyHistoryModel, Long> {
}
