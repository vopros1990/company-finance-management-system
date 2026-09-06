package com.example.company_finance_management_system.finance.service;

import com.example.company_finance_management_system.finance.api.v1.dto.request.AccountCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.AccountResponse;
import com.example.company_finance_management_system.finance.entity.Account;
import com.example.company_finance_management_system.finance.mapping.AccountMapper;
import com.example.company_finance_management_system.finance.repository.AccountRepository;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Validated
public class AccountService {

    private final AccountRepository repository;
    private final DepartmentRepository departmentRepository;
    private final AccountMapper mapper;

    public PagedModel<AccountResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAll(pageable)
                        .map(mapper::toResponse)
        );

    }

    public AccountResponse findById(@Valid @Min(value = 1, message = "Укажите корректный ID счета") Long id) {

        return mapper.toResponse(
                getById(id)
        );

    }

    @Transactional
    public AccountResponse create(@Valid AccountCreateRequest request) {

        requireDepartmentExists(request.departmentId());

        Account account = mapper.toEntity(request);

        account.setDepartment(departmentRepository.getReferenceById(request.departmentId()));

        account.setBalance(BigDecimal.ZERO);

        return mapper.toResponse(
                repository.save(account)
        );

    }

    @Transactional
    public void deleteById(@Valid @Min(value = 1, message = "Укажите корректный ID счета") Long id) {

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Счет с ID " + id + " не найден");

        repository.deleteById(id);

    }

    private Account getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Счет с ID " + id + " не найден"));

    }

    private void requireDepartmentExists(Long id) {

        if (!departmentRepository.existsById(id))
            throw new EntityNotFoundException("Подразделение с ID " + id + " не найдено");

    }

}
