package com.example.company_finance_management_system.finance.service;

import com.example.company_finance_management_system.finance.api.v1.dto.request.BudgetCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.BudgetUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.BudgetResponse;
import com.example.company_finance_management_system.finance.entity.Budget;
import com.example.company_finance_management_system.finance.mapping.BudgetMapper;
import com.example.company_finance_management_system.finance.repository.BudgetRepository;
import com.example.company_finance_management_system.finance.repository.CategoryRepository;
import com.example.company_finance_management_system.identity.entity.Department;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import com.example.company_finance_management_system.utils.PeriodUtils;
import com.example.company_finance_management_system.utils.dto.LocalDatePeriod;
import com.example.company_finance_management_system.utils.dto.PeriodType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class BudgetService {

    private final BudgetRepository repository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper mapper;

    public PagedModel<BudgetResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAll(pageable)
                        .map(mapper::toResponse)
        );

    }

    public PagedModel<BudgetResponse> findByDepartment(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID подразделения")
            Long departmentId,
            CustomUserDetails userDetails,
            Pageable pageable
    ) {

        requireDepartmentExists(departmentId);

        requireHasReadPermissions(departmentId, userDetails);

        return new PagedModel<> (
                repository.findByDepartmentId(departmentId, pageable)
                        .map(mapper::toResponse)
        );

    }

    public BudgetResponse findById(
            @Valid
            @Min(value = 1, message = "Укажите корректное значение ID бюджета")
            Long id,
            CustomUserDetails userDetails
    ) {

        Budget budget = getById(id);

        requireHasReadPermissions(budget.getDepartment().getId(), userDetails);

        return mapper.toResponse(budget);

    }

    @Transactional
    public BudgetResponse create(@Valid BudgetCreateRequest request, Long userId) {

        requireDepartmentExists(request.departmentId());

        requireCategoryExists(request.categoryId());

        Budget budget = mapper.toEntity(request);

        LocalDatePeriod period = PeriodUtils.resolvePeriod(
                request.year(),
                request.periodCount(),
                PeriodType.valueOf(request.period())
        );

        budget.setPeriodFrom(period.from());

        budget.setPeriodTo(period.to());

        budget.setDepartment(
                departmentRepository.getReferenceById(request.departmentId())
        );

        budget.setCategory(
                categoryRepository.getReferenceById(request.categoryId())
        );

        budget.setResponsibleUser(
                userRepository.getReferenceById(userId)
        );

        return mapper.toResponse(
                repository.save(budget)
        );

    }

    @Transactional
    public BudgetResponse update(
            @Valid
            BudgetUpdateRequest request,
            @Valid
            @Min(value = 1, message = "Укажите корректный ID бюджета")
            Long id,
            CustomUserDetails userDetails
    ) {

        Budget budget = getById(id);

        requireHasModifyPermissions(budget, userDetails);

        mapper.patch(budget, request);

        if (request.departmentId() != null) {

            requireDepartmentExists(request.departmentId());

            budget.setDepartment(departmentRepository.getReferenceById(request.departmentId()));

        }

        if (request.categoryId() != null) {

            requireCategoryExists(request.categoryId());

            budget.setCategory(categoryRepository.getReferenceById(request.categoryId()));

        }

        return mapper.toResponse(
                repository.save(budget)
        );

    }

    @Transactional
    public void deleteById(
            @Valid
            @Min(value = 1, message = "Укажите корректное значение ID бюджета")
            Long id
    ) {

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Бюджет с ID" + id + " не найден");

        repository.deleteById(id);

    }

    private Budget getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бюджет с ID" + id + " не найден"));

    }

    private void requireDepartmentExists(Long departmentId) {

        if(!departmentRepository.existsById(departmentId))
            throw new EntityNotFoundException("Подразделение с ID " + departmentId + " не найдено");

    }

    private void requireCategoryExists(Long categoryId) {

        if(!categoryRepository.existsById(categoryId))
            throw new EntityNotFoundException("Категория с ID " + categoryId + " не найдена");

    }

    private void requireHasModifyPermissions(Budget budget, CustomUserDetails userDetails) {

        if (UserRole.ADMIN.equals(userDetails.getRole()))
            return;

        if (!userDetails.getId().equals(budget.getResponsibleUser().getId()))
            throw new AccessDeniedException("Отсутствуют права доступа к операции");

    }

    private void requireHasReadPermissions(Long departmentId, CustomUserDetails userDetails) {

        if (UserRole.ADMIN.equals(userDetails.getRole()) || UserRole.AUDITOR.equals(userDetails.getRole()))
            return;

        Long userDepartmentId = userRepository.findById(userDetails.getId())
                .map(User::getDepartment)
                .map(Department::getId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userDetails.getId() + " не найден"));

        if (UserRole.ACCOUNTANT.equals(userDetails.getRole()) && !departmentId.equals(userDepartmentId))
            throw new AccessDeniedException("Отсутствуют права доступа к ресурсу");

    }

}
