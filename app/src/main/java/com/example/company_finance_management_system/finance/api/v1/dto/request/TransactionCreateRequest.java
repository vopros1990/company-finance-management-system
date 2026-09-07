package com.example.company_finance_management_system.finance.api.v1.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionCreateRequest(

        @NotBlank(message = "Укажите тип транзакции")
        @Pattern(regexp = "INCOME|EXPENSE|TRANSFER")
        String type,

        @NotBlank(message = "Укажите валюту транзакции")
        @Pattern(regexp = "RUB")
        String currency,

        @NotNull(message = "Укажите сумму")
        @DecimalMin(value = "0", message = "Укажите корректное значение суммы")
        BigDecimal amount,

        @NotNull(message = "Укажите ID подразделения")
        @Min(value = 1, message = "Укажите корректный ID подразделения")
        Long departmentId,

        @NotNull(message = "Укажите ID категории")
        @Min(value = 1, message = "Укажите корректный ID транзакции")
        Long categoryId,

        @NotNull(message = "Укажите ID целевого аккаунта")
        @Min(value = 1, message = "Укажите корректный ID целевого счета")
        Long accountTargetId,

        @Min(value = 1, message = "Укажите корректный ID счета-отправителя")
        Long accountFromId,

        @Min(value = 1, message = "Укажите корректный ID контрагента")
        Long counterpartyId

) {
}
