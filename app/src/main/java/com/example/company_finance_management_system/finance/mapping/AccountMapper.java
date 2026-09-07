package com.example.company_finance_management_system.finance.mapping;

import com.example.company_finance_management_system.finance.api.v1.dto.request.AccountCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.AccountResponse;
import com.example.company_finance_management_system.finance.entity.Account;
import com.example.company_finance_management_system.identity.mapping.DepartmentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {
                DepartmentMapper.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountMapper {

    AccountResponse toResponse(Account account);

    Account toEntity(AccountCreateRequest request);

}
