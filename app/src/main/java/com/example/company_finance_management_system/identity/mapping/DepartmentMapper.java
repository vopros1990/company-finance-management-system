package com.example.company_finance_management_system.identity.mapping;

import com.example.company_finance_management_system.identity.api.v1.dto.request.DepartmentCreateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.DepartmentUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;
import com.example.company_finance_management_system.identity.entity.Department;
import com.example.company_finance_management_system.identity.entity.User;
import org.mapstruct.*;

@Mapper(
        uses = {
                UserMapper.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DepartmentMapper {

    DepartmentResponse toResponse(Department department);

    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "responsibleUser", source = "responsibleUser")
    Department toEntity(DepartmentCreateRequest request, User responsibleUser);

    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "target.id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "responsibleUser", source = "responsibleUser")
    void patch(
            @MappingTarget Department target,
            DepartmentUpdateRequest request,
            User responsibleUser
    );

}
