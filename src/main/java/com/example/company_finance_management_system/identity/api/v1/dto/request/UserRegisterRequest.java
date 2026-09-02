package com.example.company_finance_management_system.identity.api.v1.dto.request;

import com.example.company_finance_management_system.identity.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(

        @NotBlank(message = "Укажите email пользователя")
        String name,

        @NotBlank(message = "Укажите email пользователя")
        @Email(message = "Укажите email пользователя в правильном формате")
        String email,

        @NotBlank(message = "Укажите пароль пользователя")
        String password,

        @NotNull(message = "Укажите ID отдела предприятия")
        @Min(value = 1, message = "ID отдела предприятия не может быть меньне 0")
        Long departmentId,

        @NotBlank(message = "Укажите роль пользователя")
        UserRole role

) {
}
