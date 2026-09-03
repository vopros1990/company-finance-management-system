package com.example.company_finance_management_system.identity.mapping;

import com.example.company_finance_management_system.identity.api.v1.dto.request.UserRegisterRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.entity.User;
import org.mapstruct.*;

@Mapper(
        uses = {
                DepartmentMapper.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponse toResponse(User user);

    User toEntity(UserRegisterRequest request, String passwordHash);

    User toEntity(UserUpdateRequest request, String passwordHash);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    void patch(@MappingTarget User target, User source);

}
