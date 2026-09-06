package com.example.company_finance_management_system.identity.api.v1.dto.response;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.OffsetDateTime;

public record UserResponse(

        Long id,

        String name,

        String email,

        UserRole role,

        @JsonView(Extended.class)
        DepartmentResponse department,

        OffsetDateTime createdAt,

        @JsonView(Extended.class)
        OffsetDateTime updatedAt

) {
}
