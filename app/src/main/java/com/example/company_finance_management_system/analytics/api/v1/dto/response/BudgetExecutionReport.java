package com.example.company_finance_management_system.analytics.api.v1.dto.response;

import com.example.company_finance_management_system.finance.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BudgetExecutionReport(

        Department department,
        Period period,
        Total total,
        List<Category> categories

) {

    public static BudgetExecutionReport empty(Long departmentId, LocalDate from, LocalDate to) {

        Department department = new Department(departmentId, null);

        Period period = new Period(from, to);

        Total total = new Total(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null
        );

        return new BudgetExecutionReport(
                department,
                period,
                total,
                List.of()
        );

    }

    public record Department(
            Long id,
            String name
    ) {}

    public record Period(
            LocalDate from,
            LocalDate to
    ) {}

    public record Total(
            BigDecimal planned,
            BigDecimal used,
            BigDecimal remaining,
            BigDecimal executionPercent,
            Currency currency
    ) {}

    public record Category(
            Long id,
            String name,
            BigDecimal planned,
            BigDecimal used,
            BigDecimal remaining,
            BigDecimal executionPercent
    ) {}

}
