package com.financeapp.backend.repository;

import com.financeapp.backend.enums.Roles;
import com.financeapp.backend.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByRole(Roles roles);

    Roles role(Roles role);
}
