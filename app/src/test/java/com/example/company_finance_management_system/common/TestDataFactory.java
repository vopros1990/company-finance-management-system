package com.example.company_finance_management_system.common;

import com.example.company_finance_management_system.analytics.persistance.projection.BudgetSummary;
import com.example.company_finance_management_system.analytics.persistance.projection.TransactionSummary;
import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionCreateRequest;
import com.example.company_finance_management_system.finance.entity.*;
import com.example.company_finance_management_system.identity.entity.Department;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDataFactory {

    public static User user(Long userId) {

        return User.builder()
                .id(userId)
                .build();

    }

    public static User user(Long userId, Long departmentId) {

        return User.builder()
                .id(userId)
                .department(department(departmentId))
                .build();

    }

    public static CustomUserDetails userDetailsMock(Long id, UserRole role) {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(userDetails.getId()).thenReturn(id);

        when(userDetails.getRole()).thenReturn(role);

        return userDetails;

    }

    public static Category category(Long id, String name) {

        return Category.builder()
                .id(id)
                .name(name)
                .build();

    }

    public static Department department(Long id) {

        return Department.builder()
                .id(id)
                .build();

    }

    public static Account account(Long id, Long departmentId, BigDecimal balance) {

        return Account.builder()
                .id(id)
                .department(department(departmentId))
                .balance(balance)
                .build();

    }

    public static Account account(Long id, Long departmentId) {

        return Account.builder()
                .id(id)
                .department(department(departmentId))
                .balance(BigDecimal.ZERO)
                .build();

    }

    public static BudgetSummary budgetSummary(LocalDate from, LocalDate to, Category category, BigDecimal amount) {

        return BudgetSummary.builder()
                .dateFrom(from)
                .dateTo(to)
                .categoryId(category.getId())
                .categoryName(category.getName())
                .amount(amount)
                .currency(Currency.RUB)
                .build();

    }

    public static TransactionSummary confirmedIncome(Long id, Department department, Category category, BigDecimal amount) {

        return TransactionSummary.builder()
                .id(id)
                .departmentId(department.getId())
                .departmentName(department.getName())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .amount(amount)
                .type(TransactionType.INCOME)
                .status(TransactionStatus.CONFIRMED)
                .currency(Currency.RUB)
                .build();

    }

    public static TransactionSummary confirmedExpense(Long id, Department department, Category category, BigDecimal amount) {

        return TransactionSummary.builder()
                .id(id)
                .departmentId(department.getId())
                .departmentName(department.getName())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .amount(amount)
                .type(TransactionType.EXPENSE)
                .status(TransactionStatus.CONFIRMED)
                .currency(Currency.RUB)
                .build();

    }

    public static TransactionSummary reversalIncome(Long id, Department department, Category category, BigDecimal amount) {

        return TransactionSummary.builder()
                .id(id)
                .departmentId(department.getId())
                .departmentName(department.getName())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .amount(amount)
                .type(TransactionType.INCOME)
                .status(TransactionStatus.REVERSED)
                .currency(Currency.RUB)
                .build();

    }

    public static TransactionSummary reversalExpense(Long id, Department department, Category category, BigDecimal amount) {

        return TransactionSummary.builder()
                .id(id)
                .departmentId(department.getId())
                .departmentName(department.getName())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .amount(amount)
                .type(TransactionType.EXPENSE)
                .status(TransactionStatus.REVERSED)
                .currency(Currency.RUB)
                .build();

    }

    public static Transaction confirmedIncome(Long id, Department department, Category category, Account account, BigDecimal amount) {

        return Transaction.builder()
                .id(id)
                .department(department)
                .category(category)
                .accountTarget(account)
                .amount(amount)
                .type(TransactionType.INCOME)
                .status(TransactionStatus.CONFIRMED)
                .build();

    }

    public static Transaction confirmedExpense(Long id, Department department, Category category, Account account, BigDecimal amount) {

        return Transaction.builder()
                .id(id)
                .department(department)
                .category(category)
                .accountTarget(account)
                .amount(amount)
                .type(TransactionType.EXPENSE)
                .status(TransactionStatus.CONFIRMED)
                .build();

    }

    public static Transaction reversalIncome(Long id, Department department, Category category, Transaction referenceTransaction) {

        return Transaction.builder()
                .id(id)
                .department(department)
                .category(category)
                .accountTarget(referenceTransaction.getAccountTarget())
                .amount(referenceTransaction.getAmount())
                .referenceTransaction(referenceTransaction)
                .type(TransactionType.INCOME)
                .status(TransactionStatus.REVERSED)
                .build();

    }

    public static Transaction reversalExpense(Long id, Department department, Category category, Account account, BigDecimal amount) {

        return Transaction.builder()
                .id(id)
                .department(department)
                .category(category)
                .accountTarget(account)
                .amount(amount)
                .type(TransactionType.EXPENSE)
                .status(TransactionStatus.REVERSED)
                .build();

    }

    public static Transaction confirmedTransfer(Long transactionId, Long authorId) {

        return Transaction.builder()
                .id(transactionId)
                .author(user(authorId))
                .status(TransactionStatus.CONFIRMED)
                .type(TransactionType.TRANSFER)
                .build();

    }

    public static Transaction confirmedTransfer(
            Long transactionId,
            Long authorId,
            Account accountTarget,
            Account accountFrom,
            BigDecimal amount
    ) {

        return Transaction.builder()
                .id(transactionId)
                .author(user(authorId))
                .status(TransactionStatus.CONFIRMED)
                .type(TransactionType.TRANSFER)
                .accountTarget(accountTarget)
                .accountFrom(accountFrom)
                .amount(amount)
                .build();

    }

    public static Transaction draftTransfer(
            Long transactionId,
            Long authorId,
            Account accountTarget,
            Account accountFrom,
            BigDecimal amount
    ) {

        return Transaction.builder()
                .id(transactionId)
                .author(user(authorId))
                .status(TransactionStatus.DRAFT)
                .type(TransactionType.TRANSFER)
                .accountTarget(accountTarget)
                .accountFrom(accountFrom)
                .amount(amount)
                .build();

    }

    public static TransactionCreateRequest transactionCreateRequest(
            TransactionType type,
            Long departmentId,
            Long accountTargetId,
            Long accountFromId,
            BigDecimal amount
    ) {

        return TransactionCreateRequest.builder()
                .type(type.toString())
                .departmentId(departmentId)
                .accountTargetId(accountTargetId)
                .accountFromId(accountFromId)
                .amount(amount)
                .build();

    }

    public static BigDecimal amount(String amount) {

        return new BigDecimal(amount);

    }

}
