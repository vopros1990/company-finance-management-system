package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentCreateRequest(

        @NotBlank(message = "Укажите название подразделения")
        @Size(min = 3, max = 255, message = "Название подразделения должно содержать от 3 до 255 символов")
        String name,

        @NotNull(message = "Укажите id пользователя, ответственного за подразделение")
        @Min(value = 1, message = "Укажите корректный ID пользователя")
        Long responsibleUserId

) {
}
