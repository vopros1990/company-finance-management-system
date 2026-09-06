package com.example.company_finance_management_system.finance.api.v1.controller;

import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.TransactionResponse;
import com.example.company_finance_management_system.finance.service.TransactionService;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUDITOR')")
    public PagedModel<TransactionResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @GetMapping("/departments/{departmentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT','ROLE_AUDITOR')")
    public PagedModel<TransactionResponse> findAllByDepartment(
            @PathVariable Long departmentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable
    ) {

        return service.findAllByDepartment(
                departmentId,
                pageable,
                userDetails
        );

    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT','ROLE_AUDITOR')")
    public TransactionResponse findById(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return service.findById(transactionId, userDetails);

    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public TransactionResponse create(
            @RequestBody TransactionCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return service.create(request, userDetails);

    }

    @PatchMapping("/{transactionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public TransactionResponse update(
            @PathVariable Long transactionId,
            @RequestBody TransactionUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return service.update(transactionId, request, userDetails);

    }

    @PatchMapping("/confirm/{transactionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public ResponseEntity<Void> confirmById(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        service.confirmById(transactionId, userDetails);

        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/reverse/{transactionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public ResponseEntity<Void> reverseById(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        service.reversalById(transactionId, userDetails);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{transactionId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTANT')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        service.deleteById(transactionId, userDetails);

        return ResponseEntity.noContent().build();

    }



}
