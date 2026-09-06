package com.example.company_finance_management_system.finance.mapping;

import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.TransactionResponse;
import com.example.company_finance_management_system.finance.entity.Transaction;
import com.example.company_finance_management_system.identity.mapping.CounterpartyMapper;
import com.example.company_finance_management_system.identity.mapping.UserMapper;
import org.mapstruct.*;

@Mapper(
        uses = {
                CategoryMapper.class,
                AccountMapper.class,
                UserMapper.class,
                CounterpartyMapper.class,
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TransactionMapper {

        TransactionResponse toResponse(Transaction transaction);

        Transaction toEntity(TransactionCreateRequest request);

        @Mapping(target = "id", ignore = true)
        Transaction clone(Transaction source);

        void patch(@MappingTarget Transaction target, TransactionUpdateRequest source);

}
