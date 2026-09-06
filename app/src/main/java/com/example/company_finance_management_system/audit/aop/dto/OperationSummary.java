package com.example.company_finance_management_system.audit.aop.dto;

import com.example.company_finance_management_system.audit.entity.OperationType;
import lombok.Builder;

@Builder
public record OperationSummary(
        Long transactionId,
        Long userId,
        OperationType operationType,
        boolean failed
) {
}
