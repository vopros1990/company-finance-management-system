package com.example.company_finance_management_system.identity.api.v1.dto.response;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.example.company_finance_management_system.identity.entity.CounterpartyType;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.OffsetDateTime;

public record CounterpartyResponse(

        Long id,

        String name,

        String inn,

        CounterpartyType type,

        @JsonView(Extended.class)
        OffsetDateTime createdAt,

        @JsonView(Extended.class)
        OffsetDateTime updatedAt
) {
}
