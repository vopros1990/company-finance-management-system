package com.example.company_finance_management_system.finance.repository;

import com.example.company_finance_management_system.finance.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE parent.id=:id")
    List<Category> findChildren(Long id);

    @Query("SELECT c.path FROM Category c WHERE c.id=:id")
    String getPathById(Long id);

}
