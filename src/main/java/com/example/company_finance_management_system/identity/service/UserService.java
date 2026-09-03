package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.identity.api.v1.dto.request.UserUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.mapping.UserMapper;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository repository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public UserResponse updateUser(
            @Valid @Min(value = 1, message = "Некорректный ID пользователя")
            Long id,
            @Valid
            UserUpdateRequest request
    ) {

        User user = getUser(id);

        if (!passwordEncoder.matches(request.passwordOld(), user.getPasswordHash()))
            throw new IllegalArgumentException("Пароли не совпадают. Укажите правильный старый пароль пользователя");

        mapper.patch(
                user,
                mapper.toEntity(
                        request,
                        passwordEncoder.encode(request.passwordNew())
                )
        );

        return mapper.toResponse(
                repository.save(user)
        );

    }

    public void deleteById(
            @Valid @Min(value = 1, message = "Некорректный ID пользователя")
            Long id
    ) {

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Пользователь с ID " + id + " не найден");

        repository.deleteById(id);

    }

    private User getUser(Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Пользователь с ID " + id + " не найден")
                );

    }
}
