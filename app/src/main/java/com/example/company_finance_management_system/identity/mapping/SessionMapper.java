package com.example.company_finance_management_system.identity.mapping;

import com.example.company_finance_management_system.identity.entity.Session;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SessionMapper {

    @Mapping(target = "id", ignore = true)
    void patch(@MappingTarget Session target, Session source);

}
