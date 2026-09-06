package com.example.company_finance_management_system.identity.api.v1.controller;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.example.company_finance_management_system.identity.api.v1.dto.request.CounterpartyCreateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.CounterpartyUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.CounterpartyResponse;
import com.example.company_finance_management_system.identity.service.CounterpartyService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/counterparties")
@RequiredArgsConstructor
public class CounterpartyController {

    private final CounterpartyService service;

    @GetMapping
    public PagedModel<CounterpartyResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @GetMapping("/{counterpartyId}")
    @JsonView(Extended.class)
    public CounterpartyResponse findById(@PathVariable Long counterpartyId) {

        return service.findById(counterpartyId);

    }

    @PostMapping
    @JsonView(Extended.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER')")
    public CounterpartyResponse create(@RequestBody CounterpartyCreateRequest request) {

        return service.create(request);

    }

    @PatchMapping("/{counterpartyId}")
    @JsonView(Extended.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER')")
    public CounterpartyResponse update(@PathVariable Long counterpartyId, @RequestBody CounterpartyUpdateRequest request) {

        return service.update(counterpartyId, request);

    }

    @DeleteMapping("/{counterpartyId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long counterpartyId) {

        service.deleteById(counterpartyId);

        return ResponseEntity.noContent().build();

    }

}
