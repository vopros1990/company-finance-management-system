package com.example.company_finance_management_system.finance.api.v1.dto.response;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.fasterxml.jackson.annotation.JsonView;

public record CategoryResponse(

        Long id,

        String name,

        @JsonView(Extended.class)
        CategoryResponse parent

) {
}
