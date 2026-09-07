package com.example.company_finance_management_system.identity.mapping;

import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;
import com.example.company_finance_management_system.identity.entity.Department;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DepartmentMapperCompactView {

    @Named("toCompactResponse")
    @Mapping(target = "responsibleUser", ignore = true)
    DepartmentResponse toCompactResponse(Department department);

}
