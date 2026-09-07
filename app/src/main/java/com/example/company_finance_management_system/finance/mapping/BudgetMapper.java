package com.example.company_finance_management_system.finance.mapping;

import com.example.company_finance_management_system.finance.api.v1.dto.request.BudgetCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.BudgetUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.BudgetResponse;
import com.example.company_finance_management_system.finance.entity.Budget;
import com.example.company_finance_management_system.identity.mapping.DepartmentMapper;
import com.example.company_finance_management_system.identity.mapping.UserMapper;
import org.mapstruct.*;

@Mapper(
        uses = {
                DepartmentMapper.class,
                UserMapper.class,
                CategoryMapper.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BudgetMapper {

    BudgetResponse toResponse(Budget budget);

    Budget toEntity(BudgetCreateRequest request);

    void patch(@MappingTarget Budget target, BudgetUpdateRequest request);

}
