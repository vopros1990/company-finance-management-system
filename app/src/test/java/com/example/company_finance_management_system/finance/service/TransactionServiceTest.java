package com.example.company_finance_management_system.finance.service;

import com.example.company_finance_management_system.common.TestDataFactory;
import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionUpdateRequest;
import com.example.company_finance_management_system.finance.entity.Account;
import com.example.company_finance_management_system.finance.entity.Transaction;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.entity.TransactionType;
import com.example.company_finance_management_system.finance.mapping.TransactionMapper;
import com.example.company_finance_management_system.finance.repository.AccountRepository;
import com.example.company_finance_management_system.finance.repository.TransactionRepository;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.DepartmentRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private TransactionService service;

    @Test
    public void whenCreate_crossDepartmentTransfer_thenThrow() {

        CustomUserDetails userDetails = TestDataFactory.userDetailsMock(1L, UserRole.ACCOUNTANT);

        Account accountTarget = TestDataFactory.account(1L, 3L, new BigDecimal("100"));

        Account accountFrom = TestDataFactory.account(2L, 4L, new BigDecimal("200"));

        Transaction transaction = TestDataFactory.draftTransfer(
                null,
                1L,
                accountTarget,
                accountFrom,
                new BigDecimal("100")
        );

        TransactionCreateRequest request = TestDataFactory.transactionCreateRequest(
                TransactionType.TRANSFER,
                3L,
                1L,
                2L,
                new BigDecimal("100")
        );

        when(mapper.toEntity(request)).thenReturn(transaction);

        when(userRepository.findById(1L)).thenReturn(Optional.of(TestDataFactory.user(1L, 3L)));

        when(departmentRepository.getReferenceById(3L)).thenReturn(TestDataFactory.department(3L));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(accountTarget));

        when(accountRepository.findById(2L)).thenReturn(Optional.of(accountFrom));

        assertThrows(IllegalArgumentException.class, () -> service.create(request, userDetails));

    }

    @Test
    public void whenUpdate_confirmedTransaction_thenThrow() {

        CustomUserDetails userDetails = TestDataFactory.userDetailsMock(1L, UserRole.ACCOUNTANT);

        Transaction transaction = TestDataFactory.confirmedTransfer(10L, 1L);

        when(transactionRepository.findById(10L))
                .thenReturn(Optional.of(transaction));

        TransactionUpdateRequest request = TransactionUpdateRequest.builder()
                .type("INCOME")
                .currency("RUB")
                .amount(new BigDecimal("1000.0"))
                .accountTargetId(2L)
                .categoryId(1L)
                .build();

        assertThrows(
                IllegalStateException.class,
                () -> service.update(10L, request, userDetails)
        );

        verify(mapper, never()).toResponse(transaction);

        verify(transactionRepository, never()).save(transaction);

    }

    @Test
    public void whenConfirm_theTransaction_thenUpdateTargetAndSourceAccounts() {

        mockClock();

        CustomUserDetails userDetails = TestDataFactory.userDetailsMock(1L, UserRole.ACCOUNTANT);

        Account accountTarget = TestDataFactory.account(2L, 3L, new BigDecimal("200"));

        Account accountFrom = TestDataFactory.account(2L, 3L, new BigDecimal("1000"));

        Transaction transaction = TestDataFactory.draftTransfer(
                10L,
                1L,
                accountTarget,
                accountFrom,
                new BigDecimal("200")
        );

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction));

        service.confirmById(10L, userDetails);

        assertEquals(new BigDecimal("400"), accountTarget.getBalance());

        assertEquals(new BigDecimal("800"), accountFrom .getBalance());

        assertEquals(TransactionStatus.CONFIRMED, transaction.getStatus());

    }

    @Test
    public void whenReversal_confirmedTransaction_thenCreateNewReversedTransaction() {

        mockClock();

        CustomUserDetails userDetails = TestDataFactory.userDetailsMock(1L, UserRole.ACCOUNTANT);

        Account accountTarget = TestDataFactory.account(2L, 3L, new BigDecimal("200"));

        Account accountFrom = TestDataFactory.account(2L, 3L, new BigDecimal("1000"));

        Transaction transaction = TestDataFactory.confirmedTransfer(
                10L,
                1L,
                accountTarget,
                accountFrom,
                new BigDecimal("200")
        );

        Transaction cloned = TestDataFactory.confirmedTransfer(
                null,
                1L,
                accountTarget,
                accountFrom,
                new BigDecimal("200")
        );

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        when(transactionRepository.findById(10L))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.getReferenceById(10L))
                .thenReturn(transaction);

        when(mapper.clone(transaction)).thenReturn(cloned);

        service.reversalById(10L, userDetails);

        verify(transactionRepository).save(captor.capture());

        Transaction captured = captor.getValue();

        verify(mapper, times(1)).clone(transaction);

        verify(transactionRepository, times(1)).save(cloned);

        verify(transactionRepository, times(1)).getReferenceById(10L);

        assertEquals(TransactionStatus.REVERSED, captured.getStatus());

        assertEquals(10L, captured.getReferenceTransaction().getId());

    }

    @Test
    public void whenReversal_confirmedTransaction_thenUpdateTargetAndSourceAccounts() {

        mockClock();

        CustomUserDetails userDetails = TestDataFactory.userDetailsMock(1L, UserRole.ACCOUNTANT);

        Account accountTarget = TestDataFactory.account(2L, 3L, new BigDecimal("200"));

        Account accountFrom = TestDataFactory.account(2L, 3L, new BigDecimal("1000"));

        Transaction transaction = TestDataFactory.confirmedTransfer(
                10L,
                1L,
                accountTarget,
                accountFrom,
                new BigDecimal("200")
        );

        Transaction cloned = TestDataFactory.confirmedTransfer(
                null,
                1L,
                accountTarget,
                accountFrom,
                new BigDecimal("200")
        );

        when(transactionRepository.findById(10L))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.getReferenceById(10L))
                .thenReturn(transaction);

        when(mapper.clone(transaction)).thenReturn(cloned);

        service.reversalById(10L, userDetails);

        assertEquals(new BigDecimal("0"), cloned.getAccountTarget().getBalance());

        assertEquals(new BigDecimal("1200"), cloned.getAccountFrom().getBalance());

    }

    private void mockClock() {
        Clock realClock = Clock.systemUTC();

        when(clock.getZone()).thenReturn(realClock.getZone());
        when(clock.instant()).thenReturn(realClock.instant());
    }


}
