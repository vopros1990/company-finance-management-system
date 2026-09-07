package com.example.company_finance_management_system.finance.api.v1.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AccountCreateRequest(

        @NotBlank(message = "Укажите тип счета")
        @Pattern(regexp = "BANK_ACCOUNT|CASH_DESK|CORPORATE_CARD")
        String type,

        @NotBlank(message = "Укажите валюту счета")
        @Pattern(regexp = "RUB")
        String currency,

        @NotNull(message = "Укажите ID подразделения")
        @Min(value = 1, message = "Укажите корректный ID подразделения")
        Long departmentId

) {
}
