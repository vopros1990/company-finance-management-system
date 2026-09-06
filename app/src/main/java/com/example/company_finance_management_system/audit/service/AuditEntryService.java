package com.example.company_finance_management_system.audit.service;

import com.example.company_finance_management_system.audit.aop.dto.OperationSummary;
import com.example.company_finance_management_system.audit.repository.AuditEntryRepository;
import com.example.company_finance_management_system.audit.api.v1.dto.response.AuditEntryResponse;
import com.example.company_finance_management_system.audit.repository.projection.AuditEntrySummary;
import com.example.company_finance_management_system.audit.entity.AuditEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditEntryService {

    private final AuditEntryRepository repository;

    public PagedModel<AuditEntryResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAllSummary(pageable)
                        .map(AuditEntryService::toResponse)
        );

    }

    public void logTransaction(OperationSummary summary) {

        AuditEntry entry = AuditEntry.builder()
                .transactionId(summary.transactionId())
                .userId(summary.userId())
                .operationType(summary.operationType())
                .build();

        repository.save(entry);

    }

    private static AuditEntryResponse toResponse(AuditEntrySummary summary) {

        return new AuditEntryResponse(
                summary.id(),
                summary.transactionId(),
                summary.transactionType(),
                summary.userId(),
                summary.userName(),
                summary.userEmail(),
                summary.operationType(),
                summary.failed(),
                summary.timestamp()
        );

    }

}
