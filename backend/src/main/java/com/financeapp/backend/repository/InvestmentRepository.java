package com.financeapp.backend.repository;


import com.financeapp.backend.model.InvestmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepository extends JpaRepository<InvestmentModel, Long> {
}
