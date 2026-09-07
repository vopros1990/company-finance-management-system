package com.example.company_finance_management_system.audit.repository;

import com.example.company_finance_management_system.audit.repository.projection.AuditEntrySummary;
import com.example.company_finance_management_system.audit.entity.AuditEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AuditEntryRepository extends JpaRepository<AuditEntry, Long>, PagingAndSortingRepository<AuditEntry, Long> {

    @Query(
            value = """
                    SELECT
                        a.id as id,
                        a.transaction_id AS transaction_id,
                        t.type AS transaction_type,
                        a.user_id AS user_id,
                        u.name AS user_name,
                        u.email AS user_email,
                        a.operation_type AS operation_type,
                        a.failed AS failed,
                        a.timestamp AS timestamp
                    FROM finance_management.audit_log a
                    LEFT JOIN finance_management.transactions t ON a.transaction_id=t.id
                    LEFT JOIN finance_management.users u ON a.user_id=u.id
                    """,
            countQuery = """
                    SELECT COUNT(1)
                    FROM finance_management.audit_log a
                    LEFT JOIN finance_management.transactions t ON a.transaction_id=t.id
                    LEFT JOIN finance_management.users u ON a.user_id=u.id
                    """,
            nativeQuery = true
    )
    Page<AuditEntrySummary> findAllSummary(Pageable pageable);

    List<AuditEntry> findByTransactionId(Long transactionId);

}
