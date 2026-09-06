package com.example.company_finance_management_system.finance.api.v1.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(

        @NotBlank(message = "Укажите имя категории")
        @Size(min = 2, max = 255, message = "Имя категории должно содержать от 2 до 255 символов")
        String name,

        @Min(value = 1, message = "Укажите корректный ID родительской категории")
        Long parentCategoryId

) {
}
