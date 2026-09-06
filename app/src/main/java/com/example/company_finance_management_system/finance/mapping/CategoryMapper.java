package com.example.company_finance_management_system.finance.mapping;

import com.example.company_finance_management_system.finance.api.v1.dto.response.CategoryResponse;
import com.example.company_finance_management_system.finance.entity.Category;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

}
