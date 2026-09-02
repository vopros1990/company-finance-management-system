package com.example.company_finance_management_system.identity.api.v1.dto.response;

import com.example.company_finance_management_system.identity.entity.UserRole;

import java.time.OffsetDateTime;

public record UserRegisterResponse(

        Long id,
        String name,
        String email,
        UserRole role,
        OffsetDateTime createdAt

) {
}
