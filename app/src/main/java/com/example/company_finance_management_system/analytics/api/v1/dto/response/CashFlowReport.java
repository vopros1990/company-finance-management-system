package com.example.company_finance_management_system.analytics.api.v1.dto.response;

import com.example.company_finance_management_system.finance.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CashFlowReport(

        Department department,
        Period period,
        Total total,
        List<Category> categories

) {

    public static CashFlowReport empty(Long departmentId, LocalDate from, LocalDate to) {

        return new CashFlowReport(
                new Department(
                        departmentId,
                        null
                ),
                new Period(from, to),
                new Total(
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        null
                ),
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
            BigDecimal income,
            BigDecimal expense,
            BigDecimal balance,
            Currency currency
    ) {}

    public record Category(
            Long id,
            String name,
            BigDecimal income,
            BigDecimal expense,
            BigDecimal balance,
            Integer transactionCount
    ) { }

}
