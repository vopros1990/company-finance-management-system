package com.example.company_finance_management_system.identity.api.v1.dto.response;

import com.example.company_finance_management_system.identity.api.v1.dto.jsonview.UserView;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.OffsetDateTime;

@JsonView(UserView.class)
public record UserResponse(

        Long id,

        String name,

        String email,

        UserRole role,

        @JsonView(UserView.Extended.class)
        DepartmentResponse department,

        OffsetDateTime createdAt,

        @JsonView(UserView.Extended.class)
        OffsetDateTime updatedAt

) {
}
