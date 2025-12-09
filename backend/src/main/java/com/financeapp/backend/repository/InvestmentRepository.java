package com.financeapp.backend.repository;

import com.financeapp.backend.model.InvestmentModel;
import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<InvestmentModel, Long> {

    List<InvestmentModel> findByUser(User user);

    Optional<InvestmentModel> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);
}
