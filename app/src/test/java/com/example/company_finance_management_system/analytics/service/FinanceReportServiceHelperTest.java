package com.example.company_finance_management_system.analytics.service;

import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.common.TestDataFactory;
import com.example.company_finance_management_system.finance.entity.Category;
import com.example.company_finance_management_system.identity.entity.Department;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.List;
import java.util.Map;

import static com.example.company_finance_management_system.common.TestDataFactory.amount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinanceReportServiceHelperTest {

    @Mock
    private Clock clock;

    @InjectMocks
    private FinanceReportServiceHelper helper;

    @Test
    public void givenTransactionSummaryList_whenGroupByCategory_thenGroupByCategory() {

        Category category1 = TestDataFactory.category(1L, "Категория 1");

        Category category2 = TestDataFactory.category(2L, "Категория 2");

        Department department = TestDataFactory.department(4L);

        TransactionSummary income1 = TestDataFactory.confirmedIncome(1L, department, category1, amount("200000"));
        TransactionSummary expense1 = TestDataFactory.confirmedExpense(1L, department, category1, amount("100"));
        TransactionSummary reversal1 = TestDataFactory.reversalExpense(1L, department, category1, expense1.amount());

        TransactionSummary income2 = TestDataFactory.confirmedIncome(1L, department, category2, amount("1000"));
        TransactionSummary expense2 = TestDataFactory.confirmedExpense(1L, department, category2, amount("300"));

        List<TransactionSummary> transactions = List.of(income1, income2, expense1, expense2, reversal1);

        Map<Long, List<TransactionSummary>> actual = helper.groupByCategory(transactions);

        assertEquals(2, actual.size());

        assertTrue(actual.containsKey(1L));

        assertTrue(actual.containsKey(2L));

        assertEquals(3, actual.get(1L).size());

        assertEquals(2, actual.get(2L).size());

        assertTrue(actual.get(1L).contains(income1));

        assertTrue(actual.get(1L).contains(expense1));

        assertTrue(actual.get(1L).contains(reversal1));

        assertTrue(actual.get(2L).contains(income2));

        assertTrue(actual.get(2L).contains(expense2));

    }

    private void mockClock() {
        Clock realClock = Clock.systemUTC();

        when(clock.getZone()).thenReturn(realClock.getZone());
        when(clock.instant()).thenReturn(realClock.instant());
    }

}
