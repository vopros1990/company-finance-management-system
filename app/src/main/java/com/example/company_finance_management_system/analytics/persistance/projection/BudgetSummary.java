package com.example.company_finance_management_system.analytics.persistance.projection;

import com.example.company_finance_management_system.finance.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetSummary(
        LocalDate dateFrom,
        LocalDate dateTo,
        Long categoryId,
        String categoryName,
        BigDecimal amount,
        Currency currency
) {
}
