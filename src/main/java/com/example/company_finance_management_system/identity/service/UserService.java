package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.mapping.UserMapper;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository repository;

    private final DepartmentRepository departmentRepository;

    private final UserMapper mapper;

    public UserResponse meUser(CustomUserDetails userDetails) {

        User user = getUser(userDetails.getId());

        return mapper.toResponse(user);

    }

    public UserResponse findById(
            @Valid @Min(value = 1, message = "Некорректный ID пользователя")
            Long userId
    ) {

        return mapper.toResponse(getUser(userId));

    }

    public PagedModel<UserResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAll(pageable)
                        .map(mapper::toResponse)
        );

    }

    public PagedModel<UserResponse> findByDepartment(
            @Valid @Min(value = 1, message = "Некорректный ID подразделения")
            Long departmentId,
            Pageable pageable
    ) {

        if (!departmentRepository.existsById(departmentId))
            throw new EntityNotFoundException("Подразделение с ID " + departmentId + " не найдено");

        Page<UserResponse> page = repository.findByDepartmentId(departmentId, pageable)
                .map(mapper::toResponse);

        return new PagedModel<>(page);

    }

    private User getUser(Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Пользователь с ID " + id + " не найден")
                );

    }

}
