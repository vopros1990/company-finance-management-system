package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.*;

public record AuthRequest(
        @NotBlank(message = "Укажите email пользователя")
        @Email(message = "Неверный формат email")
        String email,

        @NotBlank(message = "Укажите пароль пользователя от 8 до 150 символов")
        @Size(min = 8, max = 150, message = "Длина пароля должна быть от 8 до 150 символов")
        String password
) {
}
