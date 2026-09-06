package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CounterpartyUpdateRequest(

        @Size(min = 2, max = 255, message = "Наименование может содержать от 2 до 255 цифр")
        String name,

        @Size(min = 10, max = 12, message = "ИНН может содержать от 10 до 12 цифр")
        @Pattern(regexp = "[0-9]{10,12}", message = "ИНН может содержать от 10 до 12 цифр")
        String inn,

        @Pattern(regexp = "SUPPLIER|CLIENT|OTHER")
        String type

) {
}
