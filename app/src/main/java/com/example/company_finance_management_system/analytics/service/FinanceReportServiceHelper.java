package com.example.company_finance_management_system.analytics.service;

import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.finance.repository.TransactionRepository;
import com.example.company_finance_management_system.utils.PeriodUtils;
import com.example.company_finance_management_system.utils.dto.OffsetDateTimePeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceReportServiceHelper {

    private final TransactionRepository transactionRepository;
    private final Clock clock;

    public List<TransactionSummary> findTransactions(
            Long departmentId,
            LocalDate periodFrom,
            LocalDate periodTo
    ) {

        OffsetDateTimePeriod dateTimePeriod = PeriodUtils.convert(periodFrom, periodTo, clock.getZone());

        return transactionRepository.findByDepartmentIdAndPeriod(
                departmentId,
                dateTimePeriod.from(),
                dateTimePeriod.to()
        );

    }

    public Map<Long, List<TransactionSummary>> groupByCategory(List<TransactionSummary> transactions) {

        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                TransactionSummary::categoryId,
                                Collectors.toList())
                );

    }


}
