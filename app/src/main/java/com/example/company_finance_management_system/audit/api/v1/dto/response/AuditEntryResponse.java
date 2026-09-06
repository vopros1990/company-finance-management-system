package com.example.company_finance_management_system.audit.api.v1.dto.response;

import com.example.company_finance_management_system.audit.entity.OperationType;
import com.example.company_finance_management_system.finance.entity.TransactionType;

import java.time.OffsetDateTime;

public record AuditEntryResponse(
        Long id,
        Long transactionId,
        TransactionType transactionType,
        Long userId,
        String userName,
        String userEmail,
        OperationType operationType,
        Boolean failed,
        OffsetDateTime timestamp
) {
}
