package com.example.company_finance_management_system.finance.api.v1.controller;

import com.example.company_finance_management_system.finance.api.v1.dto.request.AccountCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.AccountResponse;
import com.example.company_finance_management_system.finance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT','ROLE_AUDITOR')")
    public PagedModel<AccountResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT','ROLE_AUDITOR')")
    public AccountResponse findById(@PathVariable Long accountId) {

        return service.findById(accountId);

    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public AccountResponse create(@RequestBody AccountCreateRequest request) {

        return service.create(request);

    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public ResponseEntity<Void> deleteById(@PathVariable Long accountId) {

        service.deleteById(accountId);

        return ResponseEntity.noContent().build();

    }

}
