package com.example.company_finance_management_system.finance.api.v1.dto.response;

import com.example.company_finance_management_system.finance.entity.Currency;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.entity.TransactionType;
import com.example.company_finance_management_system.identity.api.v1.dto.response.CounterpartyResponse;
import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionResponse(

        Long id,
        TransactionType type,
        TransactionStatus status,
        Currency currency,
        BigDecimal amount,
        DepartmentResponse department,
        CategoryResponse category,
        UserResponse author,
        AccountResponse accountTarget,
        AccountResponse accountFrom,
        CounterpartyResponse counterparty,
        TransactionResponse referenceTransaction,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime processedAt

) {
}
