package com.example.company_finance_management_system.audit.entity;

import com.example.company_finance_management_system.finance.entity.Transaction;
import com.example.company_finance_management_system.identity.entity.Department;
import com.example.company_finance_management_system.identity.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_log", schema = "finance_management")
@Data
@Builder
public class AuditLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;

}
