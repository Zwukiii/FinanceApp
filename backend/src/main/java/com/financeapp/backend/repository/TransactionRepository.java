package com.financeapp.backend.repository;

import com.financeapp.backend.model.TransactionModel;
import com.financeapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {

    List<TransactionModel> findByUser(User user);

    Optional<TransactionModel> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);
}
