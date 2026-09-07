package com.example.company_finance_management_system.finance.api.v1.controller;

import com.example.company_finance_management_system.finance.api.v1.dto.request.BudgetCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.BudgetUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.BudgetResponse;
import com.example.company_finance_management_system.finance.service.BudgetService;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/budgets")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN','FINANCE_MANAGER')")
public class BudgetController {

    private final BudgetService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER','AUDITOR')")
    public PagedModel<BudgetResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER','AUDITOR', 'ACCOUNTANT')")
    public PagedModel<BudgetResponse> findAllByDepartmentId(
            @PathVariable Long departmentId,
            @AuthenticationPrincipal CustomUserDetails user,
            Pageable pageable
    ) {

        return service.findByDepartment(departmentId, user, pageable);

    }

    @GetMapping("/{budgetId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER','AUDITOR', 'ACCOUNTANT')")
    public BudgetResponse findById(@PathVariable Long budgetId, @AuthenticationPrincipal CustomUserDetails user) {

        return service.findById(budgetId, user);

    }

    @PostMapping
    public BudgetResponse create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody BudgetCreateRequest request
    ) {

        return service.create(request, user.getId());

    }

    @PatchMapping("/{budgetId}")
    public BudgetResponse update(
            @PathVariable Long budgetId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody BudgetUpdateRequest request
    ) {

        return service.update(request, budgetId, user);

    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long budgetId) {

        service.deleteById(budgetId);

        return ResponseEntity.noContent().build();

    }

}
