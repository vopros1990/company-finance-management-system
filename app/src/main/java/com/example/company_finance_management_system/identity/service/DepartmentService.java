package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.identity.api.v1.dto.request.DepartmentCreateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.DepartmentUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;
import com.example.company_finance_management_system.identity.entity.Department;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.mapping.DepartmentMapper;
import com.example.company_finance_management_system.identity.mapping.DepartmentMapperCompactView;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class DepartmentService {

    private final DepartmentRepository repository;
    private final UserRepository userRepository;
    private final DepartmentMapper mapper;
    private final DepartmentMapperCompactView mapperCompactView;

    @Transactional(readOnly = true)
    public PagedModel<DepartmentResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAll(pageable)
                        .map(mapperCompactView::toCompactResponse)
        );

    }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(
            @Valid @Min(value = 1, message = "Укажите корректное значение ID подразделения")
            Long id
    ) {

        return mapper.toResponse(
                getById(id)
        );

    }

    @Transactional
    public DepartmentResponse create(@Valid DepartmentCreateRequest request) {

        User user = getUserById(request.responsibleUserId());

        Department department = repository.save(
                mapper.toEntity(request, user)
        );

        repository.flush();

        user.setDepartment(department);

        userRepository.save(user);

        return mapper.toResponse(department);

    }

    @Transactional
    public DepartmentResponse update(
            @Valid @Min(value = 1, message = "Укажите корректное значение ID подразделения")
            Long id,
            @Valid
            DepartmentUpdateRequest request
    ) {

        Department department = getById(id);

        User user = getUserById(request.responsibleUserId());

        mapper.patch(department, request, user);

        department = repository.save(department);

        user.setDepartment(department);

        userRepository.save(user);

        return mapper.toResponse(department);

    }

    @Transactional
    public void deleteById(
            @Valid @Min(value = 1, message = "Укажите корректное значение ID подразделения")
            Long id
    ) {

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Подразделение с ID " + id + " не найдено");

        repository.deleteById(id);

    }

    private Department getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Подразделение с ID " + id + " не найдено")
        );

    }

    private User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

    }

}
