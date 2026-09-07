package com.example.company_finance_management_system.analytics.service;

import com.example.company_finance_management_system.analytics.api.v1.dto.request.BudgetExecutionReportRequest;
import com.example.company_finance_management_system.analytics.api.v1.dto.response.BudgetExecutionReport;
import com.example.company_finance_management_system.analytics.persistance.projection.BudgetSummary;
import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.common.service.CurrencyConverter;
import com.example.company_finance_management_system.finance.entity.Currency;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.entity.TransactionType;
import com.example.company_finance_management_system.finance.repository.BudgetRepository;
import com.example.company_finance_management_system.utils.BigDecimalUtils;
import com.example.company_finance_management_system.utils.PeriodUtils;
import com.example.company_finance_management_system.utils.dto.LocalDatePeriod;
import com.example.company_finance_management_system.utils.dto.PeriodType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class BudgetExecutionReportService {

    private final BudgetRepository budgetRepository;
    private final FinanceReportServiceHelper helper;
    private final CurrencyConverter converter;

    public BudgetExecutionReport budgetExecutionReport(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID подразделения")
            Long departmentId,
            @Valid
            BudgetExecutionReportRequest request
    ) {

        LocalDatePeriod datePeriod = PeriodUtils.resolvePeriod(
                request.year(),
                request.periodCount(),
                PeriodType.valueOf(request.type())
        );

        List<TransactionSummary> transactions = helper.findTransactions(
                departmentId,
                datePeriod.from(),
                datePeriod.to()
        );

        if (transactions.isEmpty())
            return BudgetExecutionReport.empty(
                    departmentId,
                    datePeriod.from(),
                    datePeriod.to()
            );

        Currency currency = Currency.valueOf(request.currency());

        List<BudgetSummary> budgets = findBudgets(
                departmentId,
                datePeriod.from(),
                datePeriod.to()
        );

        Map<Long, List<TransactionSummary>> transactionsByCategory = helper.groupByCategory(transactions);

        Map<Long, BudgetSummary> budgetsByCategory = groupBudgetsByCategory(budgets);

        return new BudgetExecutionReport(
                new BudgetExecutionReport.Department(
                        departmentId,
                        transactions.getFirst().departmentName()
                ),
                new BudgetExecutionReport.Period(
                        datePeriod.from(),
                        datePeriod.to()
                ),
                mapToTotal(transactions, budgets, currency),
                mapToCategoryList(
                        transactionsByCategory,
                        budgetsByCategory,
                        currency
                )
        );

    }

    private List<BudgetSummary> findBudgets(Long departmentId, LocalDate from, LocalDate to) {

        return budgetRepository.findByDepartmentIdAndPeriod(departmentId, from, to);

    }

    private Map<Long, BudgetSummary> groupBudgetsByCategory(List<BudgetSummary> budgets) {

        return budgets.stream()
                .collect(
                        Collectors.toMap(
                                BudgetSummary::categoryId,
                                budgetSummary -> budgetSummary,
                                (b1, b2) -> b2,
                                HashMap::new
                        )
                );

    }

    private BigDecimal countTotalExpenses(List<TransactionSummary> transactions, Currency currency) {

        return transactions.stream()
                .map((transaction) -> {

                    BigDecimal amount = BigDecimal.ZERO;

                    if (TransactionType.EXPENSE.equals(transaction.type()))
                        amount = converter.convert(
                                transaction.amount(),
                                transaction.currency(),
                                currency
                        );

                    if (TransactionStatus.REVERSED.equals(transaction.status()))
                        return amount.negate();

                    return amount;

                }).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BudgetExecutionReport.Total mapToTotal(
            List<TransactionSummary> transactions,
            List<BudgetSummary> budgets,
            Currency currency
    ) {

        BigDecimal expenses = countTotalExpenses(transactions, currency);

        BigDecimal planned = budgets.stream()
                .map(BudgetSummary::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BudgetExecutionReport.Total(
                planned,
                expenses,
                planned.subtract(expenses),
                BigDecimalUtils.conversionRatePercents(expenses, planned),
                currency
        );

    }

    private List<BudgetExecutionReport.Category> mapToCategoryList(
            Map<Long, List<TransactionSummary>> transactions,
            Map<Long, BudgetSummary> budgets,
            Currency currency
    ) {

        return transactions.entrySet().stream()
                .map((entry) -> {

                    Long categoryId = entry.getKey();

                    String categoryName = entry.getValue().getFirst().categoryName();

                    BudgetSummary budget = budgets.get(categoryId);

                    BigDecimal used = countTotalExpenses(entry.getValue(), currency);

                    BigDecimal planned = budget == null ?
                            BigDecimal.ZERO :
                            converter.convert(
                                    budget.amount(),
                                    budget.currency(),
                                    currency
                            );

                    return new BudgetExecutionReport.Category(
                            categoryId,
                            categoryName,
                            planned,
                            used,
                            planned.subtract(used),
                            BigDecimalUtils.conversionRatePercents(used, planned)
                    );

                }).toList();

    }

}
