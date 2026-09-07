package com.example.company_finance_management_system.identity.mapping;

import com.example.company_finance_management_system.identity.api.v1.dto.request.CounterpartyCreateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.CounterpartyUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.CounterpartyResponse;
import com.example.company_finance_management_system.identity.entity.Counterparty;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CounterpartyMapper {

    CounterpartyResponse toResponse(Counterparty counterparty);

    Counterparty toEntity(CounterpartyCreateRequest request);

    void patch(@MappingTarget Counterparty target, CounterpartyUpdateRequest request);

}
