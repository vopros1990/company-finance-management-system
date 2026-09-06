package com.example.company_finance_management_system.finance.api.v1.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record BudgetCreateRequest(

        @NotNull(message = "Укажите ID подразделения")
        @Min(value = 1, message = "Укажите корректный ID подразделения")
        Long departmentId,

        @NotNull(message = "Укажите ID категории")
        @Min(value = 1, message = "Укажите корректный ID категории")
        Long categoryId,

        @NotNull(message = "Укажите размер бюджета")
        @DecimalMin(value = "0", message = "Бюджет не может иметь отрицательное значение")
        BigDecimal amount,

        @NotBlank(message = "Укажите валюту")
        @Pattern(regexp = "RUB")
        String currency,

        @NotNull(message = "Укажите год")
        @Range(min = 2000, max = 2100, message = "Укажите год от 2000 до 2100")
        Integer year,

        @NotBlank(message = "Укажите тип периода (месяц/квартал")
        @Pattern(regexp = "MONTH|QUARTER")
        String period,

        @NotNull(message = "Укажите номер периода")
        @Range(min = 1, max = 12)
        Integer periodCount

) {
}
