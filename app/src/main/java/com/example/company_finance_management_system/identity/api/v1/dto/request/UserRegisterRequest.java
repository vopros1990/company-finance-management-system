package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @NotBlank(message = "Укажите имя пользователя")
        @Size(min = 3, max = 255, message = "Имя должно иметь длину от 2 до 255 символов")
        String name,

        @NotBlank(message = "Укажите email пользователя")
        @Email(message = "Укажите email пользователя в правильном формате")
        String email,

        @NotBlank(message = "Укажите пароль пользователя")
        @Size(min = 8, max = 100, message = "Пароль должен иметь длину от 8 до 100 символов")
        String password,

        @NotBlank(message = "Укажите роль пользователя")
        @Pattern(regexp = "FINANCE_MANAGER|ACCOUNTANT|AUDITOR")
        String role

) {
}
