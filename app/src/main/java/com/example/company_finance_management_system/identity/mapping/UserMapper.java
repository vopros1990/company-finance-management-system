package com.example.company_finance_management_system.identity.mapping;

import com.example.company_finance_management_system.identity.api.v1.dto.request.UserRegisterRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.entity.User;
import org.mapstruct.*;

@Mapper(
        uses = {
                DepartmentMapperCompactView.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "department", qualifiedByName = "toCompactResponse")
    UserResponse toResponse(User user);

    User toEntity(UserRegisterRequest request, String passwordHash);

    User toEntity(UserUpdateRequest request, String passwordHash);

    void patch(@MappingTarget User target, UserUpdateRequest source);

}
