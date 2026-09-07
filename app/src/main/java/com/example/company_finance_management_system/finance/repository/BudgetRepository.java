package com.example.company_finance_management_system.finance.repository;

import com.example.company_finance_management_system.analytics.persistance.projection.BudgetSummary;
import com.example.company_finance_management_system.finance.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long>, PagingAndSortingRepository<Budget, Long> {

    Page<Budget> findByDepartmentId(Long departmentId, Pageable pageable);

    @Query("""
            SELECT
                b.periodFrom AS dateFrom,
                b.periodTo AS dateTo,
                b.category.id AS categoryId,
                b.category.name AS categoryName,
                b.amount AS amount,
                b.currency AS currency
            FROM Budget b
            WHERE b.department.id=:departmentId
            AND b.periodFrom >= :from
            AND b.periodTo <= :to
            """)
    List<BudgetSummary> findByDepartmentIdAndPeriod(Long departmentId, LocalDate from, LocalDate to);

}
