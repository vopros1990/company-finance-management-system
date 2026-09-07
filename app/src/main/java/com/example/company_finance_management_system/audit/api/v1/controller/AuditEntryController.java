package com.example.company_finance_management_system.audit.api.v1.controller;

import com.example.company_finance_management_system.audit.api.v1.dto.response.AuditEntryResponse;
import com.example.company_finance_management_system.audit.service.AuditEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/finance/transactions/log")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AUDITOR')")
public class AuditEntryController {

    private final AuditEntryService service;

    @GetMapping
    public PagedModel<AuditEntryResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

}
