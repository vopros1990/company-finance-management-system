package com.example.company_finance_management_system.analytics.service;

import com.example.company_finance_management_system.analytics.api.v1.dto.response.CashFlowReport;
import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.common.service.CurrencyConverter;
import com.example.company_finance_management_system.finance.entity.Currency;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.repository.BudgetRepository;
import com.example.company_finance_management_system.finance.repository.TransactionRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Validated
public class CashFlowReportService {

    private final FinanceReportServiceHelper helper;
    private final CurrencyConverter converter;

    public CashFlowReport cashFlowReport(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID подразделения")
            Long departmentId,
            LocalDate periodFrom,
            LocalDate periodTo,
            @Valid
            @Pattern(regexp = "RUB")
            String currencyAlias
    ) {

        List<TransactionSummary> transactions = helper.findTransactions(departmentId, periodFrom, periodTo);

        if (transactions.isEmpty())
            return CashFlowReport.empty(departmentId, periodFrom, periodTo);

        Map<Long, List<TransactionSummary>> groupedByCategory = helper.groupByCategory(transactions);

        Currency currency = Currency.valueOf(currencyAlias);

        TotalCounter total = countTotal(transactions, currency);

        List<CashFlowReport.Category> categories = mapToCategoryList(groupedByCategory, currency);

        return new CashFlowReport(
                new CashFlowReport.Department(
                        departmentId,
                        transactions.getFirst().departmentName()
                ),
                new CashFlowReport.Period(
                        periodFrom,
                        periodTo
                ),
                new CashFlowReport.Total(
                        total.income,
                        total.expense,
                        total.income.subtract(total.expense),
                        currency
                ),
                categories
        );

    }

    private TotalCounter countTotal(List<TransactionSummary> transactions, Currency currency) {

        return transactions.stream()
                .map((transaction) -> {

                    TotalCounter total = switch (transaction.type()) {

                        case INCOME -> new TotalCounter(
                                converter.convert(
                                        transaction.amount(),
                                        transaction.currency(),
                                        currency
                                ),
                                BigDecimal.ZERO
                        );

                        case EXPENSE -> new TotalCounter(
                                BigDecimal.ZERO,
                                converter.convert(
                                        transaction.amount(),
                                        transaction.currency(),
                                        currency
                                )
                        );

                        default -> TotalCounter.empty();

                    };

                    if (TransactionStatus.REVERSED.equals(transaction.status()))
                        return total.swap();

                    return total;

                }).reduce(TotalCounter.empty(), TotalCounter::sum);

    }

    private List<CashFlowReport.Category> mapToCategoryList(
            Map<Long, List<TransactionSummary>> groupedByCategory,
            Currency currency
    ) {

        return groupedByCategory.entrySet().stream()
                .map((entry) -> {

                    Long categoryId = entry.getKey();

                    String categoryName = entry.getValue().getFirst().categoryName();

                    TotalCounter total = countTotal(entry.getValue(), currency);

                    return new CashFlowReport.Category(
                            categoryId,
                            categoryName,
                            total.income,
                            total.expense,
                            total.income.subtract(total.expense),
                            entry.getValue().size()
                    );

                }).toList();

    }

    @AllArgsConstructor
    private static class TotalCounter {

        BigDecimal income;

        BigDecimal expense;

        public TotalCounter sum(TotalCounter accumulator) {

            return new TotalCounter(
                    income.add(accumulator.income),
                    expense.add(accumulator.expense)
            );

        }

        public TotalCounter swap() {

            return new TotalCounter(
                    expense,
                    income
            );

        }

        public static TotalCounter empty() {

            return new TotalCounter(BigDecimal.ZERO, BigDecimal.ZERO);

        }

    }

}
