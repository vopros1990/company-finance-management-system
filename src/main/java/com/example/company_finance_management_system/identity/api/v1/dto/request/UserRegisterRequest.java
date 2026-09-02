package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(

        @NotBlank(message = "Укажите имя пользователя")
        String name,

        @NotBlank(message = "Укажите email пользователя")
        @Email(message = "Укажите email пользователя в правильном формате")
        String email,

        @NotBlank(message = "Укажите пароль пользователя")
        String password,

        @NotBlank(message = "Укажите роль пользователя")
        @Pattern(regexp = "FINANCE_MANAGER|ACCOUNTANT|AUDITOR")
        String role

) {
}
