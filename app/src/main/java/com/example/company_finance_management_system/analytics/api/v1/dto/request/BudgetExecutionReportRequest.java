package com.example.company_finance_management_system.analytics.api.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Range;

public record BudgetExecutionReportRequest(

        @NotNull(message = "Укажите год")
        @Range(min = 2000, max = 2100)
        Integer year,

        @NotBlank(message = "Укажите тип отчета (за месяц/квартал)")
        @Pattern(regexp = "MONTH|QUARTER")
        String type,

        @NotNull(message = "Укажите номер отчетного периода")
        Integer periodCount,

        @NotNull(message = "Укажите валюту")
        @Pattern(regexp = "RUB")
        String currency
) {
}
