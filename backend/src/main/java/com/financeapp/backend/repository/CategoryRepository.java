package com.financeapp.backend.repository;

import com.financeapp.backend.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<CategoryModel, Long> {

    boolean existsByName(String s);

}
