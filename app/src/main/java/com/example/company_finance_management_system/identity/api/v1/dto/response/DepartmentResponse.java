package com.example.company_finance_management_system.identity.api.v1.dto.response;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.OffsetDateTime;

public record DepartmentResponse(

        Long id,

        String name,

        @JsonView(Extended.class)
        UserResponse responsibleUser,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt

) {
}
