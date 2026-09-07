package com.example.company_finance_management_system.analytics.service;

import com.example.company_finance_management_system.analytics.api.v1.dto.response.CashFlowReport;
import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.common.TestDataFactory;
import com.example.company_finance_management_system.common.service.CurrencyConverter;
import com.example.company_finance_management_system.finance.entity.Category;
import com.example.company_finance_management_system.identity.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.example.company_finance_management_system.common.TestDataFactory.amount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashFlowReportServiceTest {

    @Mock
    private FinanceReportServiceHelper helper;

    private CashFlowReportService service;

    @BeforeEach
    public void setUp() {

        service = new CashFlowReportService(
                helper,
                new CurrencyConverter()
        );

    }

    @Test
    public void whenRequestByPeriod_thenReturnAggregatedReport() {

        LocalDate from = LocalDate.of(2026,8,1);

        LocalDate to = LocalDate.of(2026,8,31);

        Category category1 = TestDataFactory.category(1L, "Категория 1");

        Category category2 = TestDataFactory.category(2L, "Категория 2");

        Department department = TestDataFactory.department(4L);

        TransactionSummary income1 = TestDataFactory.confirmedIncome(1L, department, category1, amount("100"));
        TransactionSummary expense1 = TestDataFactory.confirmedExpense(1L, department, category1, amount("100"));
        TransactionSummary reversal1 = TestDataFactory.reversalIncome(1L, department, category1, income1.amount());

        TransactionSummary income2 = TestDataFactory.confirmedIncome(1L, department, category2, amount("1000"));
        TransactionSummary expense2 = TestDataFactory.confirmedExpense(1L, department, category2, amount("300"));
        TransactionSummary reversal2 = TestDataFactory.reversalExpense(1L, department, category2, expense2.amount());

        List<TransactionSummary> transactions = List.of(income1, income2, expense1, expense2, reversal1, reversal2);

        Map<Long, List<TransactionSummary>> grouped = Map.of(
                1L, List.of(income1, expense1, reversal1),
                2L, List.of(income2, expense2, reversal2)
        );

        when(helper.findTransactions(4L, from, to)).thenReturn(transactions);

        when(helper.groupByCategory(transactions)).thenReturn(grouped);

        CashFlowReport report = service.cashFlowReport(
                4L,
                from,
                to,
                "RUB"
        );

        BigDecimal categoryBalance1 = report.categories().stream()
                .filter(c -> c.name().equals("Категория 1"))
                .map(CashFlowReport.Category::balance)
                .findFirst()
                .get();

        BigDecimal categoryBalance2 = report.categories().stream()
                .filter(c -> c.name().equals("Категория 2"))
                .map(CashFlowReport.Category::balance)
                .findFirst()
                .get();

        assertEquals(4L, report.department().id());

        assertEquals(from, report.period().from());

        assertEquals(to, report.period().to());

        assertThat(amount("900")).isEqualByComparingTo(
                report.total().balance());

        assertEquals(2, report.categories().size());

        assertThat(amount("-100")).isEqualByComparingTo(categoryBalance1);

        assertThat(amount("1000")).isEqualByComparingTo(categoryBalance2);

    }

}
