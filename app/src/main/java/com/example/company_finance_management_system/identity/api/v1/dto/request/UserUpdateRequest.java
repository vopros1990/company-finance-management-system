package com.example.company_finance_management_system.identity.api.v1.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(

        String name,

        @Pattern(regexp = "FINANCE_MANAGER|ACCOUNTANT|AUDITOR")
        String role

) {
}
