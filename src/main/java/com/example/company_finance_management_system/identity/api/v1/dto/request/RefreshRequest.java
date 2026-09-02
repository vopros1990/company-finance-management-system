package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefreshRequest(

        @NotNull(message = "Укажите refresh-токен")
        String refreshToken

) {
}
