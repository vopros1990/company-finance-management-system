package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.identity.api.v1.dto.request.CounterpartyCreateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.CounterpartyUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.CounterpartyResponse;
import com.example.company_finance_management_system.identity.entity.Counterparty;
import com.example.company_finance_management_system.identity.mapping.CounterpartyMapper;
import com.example.company_finance_management_system.identity.repository.CounterpartyRepository;
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
public class CounterpartyService {

    private final CounterpartyRepository repository;
    private final CounterpartyMapper mapper;

    public PagedModel<CounterpartyResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAll(pageable)
                        .map(mapper::toResponse)
        );

    }

    public CounterpartyResponse findById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID контрагента")
            Long id
    ) {

        return mapper.toResponse(
                getById(id)
        );

    }

    @Transactional
    public CounterpartyResponse create(@Valid CounterpartyCreateRequest request) {

        Counterparty counterparty = mapper.toEntity(request);

        return mapper.toResponse(
                repository.save(counterparty)
        );

    }

    @Transactional
    public CounterpartyResponse update(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID контрагента")
            Long id,
            @Valid
            CounterpartyUpdateRequest request
    ) {

        Counterparty counterparty = getById(id);

        mapper.patch(counterparty, request);

        return mapper.toResponse(
                repository.save(counterparty)
        );

    }

    @Transactional
    public void deleteById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID контрагента")
            Long id
    ) {

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Контрагент с ID " + id + " не найден");

        repository.deleteById(id);

    }

    private Counterparty getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Контрагент с ID " + id + " не найден"));

    }

}
