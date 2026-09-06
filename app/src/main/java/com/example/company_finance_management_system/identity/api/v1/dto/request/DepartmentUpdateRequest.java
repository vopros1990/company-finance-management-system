package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(

        @Size(min = 3, max = 255, message = "Название подразделения должно содержать от 3 до 255 символов")
        String name,

        @Min(value = 1, message = "Укажите корректный ID пользователя")
        Long responsibleUserId

) {
}
