package com.example.company_finance_management_system.finance.api.v1.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionUpdateRequest(

        @Pattern(regexp = "INCOME|EXPENSE|TRANSFER")
        String type,

        @Pattern(regexp = "RUB")
        String currency,

        @DecimalMin(value = "0", message = "Укажите корректное значение суммы")
        BigDecimal amount,

        @Min(value = 1, message = "Укажите корректный ID транзакции")
        Long categoryId,

        @Min(value = 1, message = "Укажите корректный ID целевого счета")
        Long accountTargetId,

        @Min(value = 1, message = "Укажите корректный ID счета-отправителя")
        Long accountFromId,

        @Min(value = 1, message = "Укажите корректный ID контрагента")
        Long counterpartyId

) {
}
