package com.example.company_finance_management_system.finance.api.v1.dto.response;

import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;
import com.example.company_finance_management_system.finance.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record BudgetResponse(
        Long id,
        DepartmentResponse department,
        CategoryResponse category,
        BigDecimal amount,
        Currency currency,
        LocalDate periodFrom,
        LocalDate periodTo,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
