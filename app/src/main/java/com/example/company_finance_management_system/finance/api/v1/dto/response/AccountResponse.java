package com.example.company_finance_management_system.finance.api.v1.dto.response;

import com.example.company_finance_management_system.finance.entity.AccountType;
import com.example.company_finance_management_system.finance.entity.Currency;
import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AccountResponse(

        Long id,

        AccountType type,

        Currency currency,

        BigDecimal balance,

        DepartmentResponse department,

        OffsetDateTime createdAt
) {
}
