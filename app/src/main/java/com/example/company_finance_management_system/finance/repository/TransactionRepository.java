package com.example.company_finance_management_system.finance.repository;

import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.finance.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends
        JpaRepository<Transaction, Long>,
        PagingAndSortingRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction>
{

    @EntityGraph(
            attributePaths = {
                    "department",
                    "category",
                    "accountTarget",
                    "accountFrom",
                    "author",
                    "counterparty"
            })
    Optional<Transaction> findById(Long id);

    @Query("SELECT t FROM Transaction t WHERE t.accountTarget.department.id=:departmentId")
    Page<Transaction> findAllByDepartmentId(Long departmentId, Pageable pageable);

    @Query("""
            SELECT
                t.id AS id,
                t.department.id AS departmentId,
                t.department.name AS departmentName,
                t.category.id AS categoryId,
                t.category.name AS categoryName,
                t.type AS type,
                t.status AS status,
                t.amount AS amount,
                t.currency AS currency
            FROM Transaction t
            WHERE t.department.id=:departmentId
            AND t.processedAt >= :from
            AND t.processedAt <= :to
            AND (t.status = 'CONFIRMED' OR t.status = 'REVERSED')
            AND t.type <> 'TRANSFER'
            """)
    List<TransactionSummary> findByDepartmentIdAndPeriodNotTransfer(Long departmentId, OffsetDateTime from, OffsetDateTime to);

}
