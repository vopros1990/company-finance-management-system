package com.example.company_finance_management_system.finance.api.v1.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BudgetUpdateRequest(

        @Min(value = 1, message = "Укажите корректный ID подразделения")
        Long departmentId,

        @Min(value = 1, message = "Укажите корректный ID категории")
        Long categoryId,

        @DecimalMin(value = "0", message = "Бюджет не может иметь отрицательное значение")
        BigDecimal amount,

        @Pattern(regexp = "RUB")
        String currency

) {
}
