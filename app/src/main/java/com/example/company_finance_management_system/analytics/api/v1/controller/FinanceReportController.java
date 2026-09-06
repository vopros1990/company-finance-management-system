package com.example.company_finance_management_system.analytics.api.v1.controller;

import com.example.company_finance_management_system.analytics.api.v1.dto.request.BudgetExecutionReportRequest;
import com.example.company_finance_management_system.analytics.api.v1.dto.response.BudgetExecutionReport;
import com.example.company_finance_management_system.analytics.api.v1.dto.response.CashFlowReport;
import com.example.company_finance_management_system.analytics.service.BudgetExecutionReportService;
import com.example.company_finance_management_system.analytics.service.CashFlowReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/finance/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class FinanceReportController {

    private final CashFlowReportService cashFlowRReportService;
    private final BudgetExecutionReportService budgetExecutionReportService;

    @GetMapping("/departments/{departmentId}/cash-flow")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER','ROLE_AUDITOR')")
    public CashFlowReport cashFlowReport(
            @PathVariable Long departmentId,
            @RequestParam LocalDate periodFrom,
            @RequestParam LocalDate periodTo,
            @RequestParam String currency
            ) {

        return cashFlowRReportService.cashFlowReport(
                departmentId,
                periodFrom,
                periodTo,
                currency
        );

    }

    @GetMapping("/departments/{departmentId}/budget-execution")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER','ROLE_AUDITOR')")
    public BudgetExecutionReport budgetExecutionReport(
            @PathVariable Long departmentId,
            BudgetExecutionReportRequest request
    ) {

        return budgetExecutionReportService.budgetExecutionReport(departmentId, request);

    }
}
