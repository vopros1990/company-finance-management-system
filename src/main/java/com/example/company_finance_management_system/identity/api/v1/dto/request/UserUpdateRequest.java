package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        String name,

        @NotBlank(message = "Укажите старый пароль пользователя")
        String passwordOld,

        @Size(min = 8, max = 100, message = "Новый пароль должен иметь длину от 8 до 100 символов")
        String passwordNew,

        @Pattern(regexp = "FINANCE_MANAGER|ACCOUNTANT|AUDITOR")
        String role

) {
}
