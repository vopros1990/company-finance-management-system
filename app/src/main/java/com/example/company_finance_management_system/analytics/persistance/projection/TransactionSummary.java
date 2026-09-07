package com.example.company_finance_management_system.analytics.persistance.projection;

import com.example.company_finance_management_system.finance.entity.Currency;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.entity.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionSummary(
        Long id,
        Long departmentId,
        String departmentName,
        Long categoryId,
        String categoryName,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        Currency currency
) {
}
