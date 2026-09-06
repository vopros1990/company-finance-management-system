package com.example.company_finance_management_system.finance.service;

import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.TransactionUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.TransactionResponse;
import com.example.company_finance_management_system.finance.entity.Account;
import com.example.company_finance_management_system.finance.entity.Transaction;
import com.example.company_finance_management_system.finance.entity.TransactionStatus;
import com.example.company_finance_management_system.finance.entity.TransactionType;
import com.example.company_finance_management_system.finance.exception.NotEnoughFundsException;
import com.example.company_finance_management_system.finance.mapping.TransactionMapper;
import com.example.company_finance_management_system.finance.repository.AccountRepository;
import com.example.company_finance_management_system.finance.repository.CategoryRepository;
import com.example.company_finance_management_system.finance.repository.TransactionRepository;
import com.example.company_finance_management_system.identity.entity.Department;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.CounterpartyRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class TransactionService {

    private final TransactionRepository repository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final UserRepository userRepository;
    private final TransactionMapper mapper;
    private final Clock clock;

    public PagedModel<TransactionResponse> findAll(Pageable pageable) {

        return new PagedModel<>(
                repository.findAll(pageable)
                        .map(mapper::toResponse)
        );

    }

    public PagedModel<TransactionResponse> findAllByDepartment(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID подразделения")
            Long departmentId,
            Pageable pageable,
            CustomUserDetails userDetails
    ) {

        requireHasDepartmentAuditAccess(departmentId, userDetails);

        return new PagedModel<>(
                repository.findAllByDepartmentId(departmentId, pageable)
                        .map(mapper::toResponse)
        );

    }

    @Transactional
    public TransactionResponse findById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID транзакции")
            Long id,
            CustomUserDetails userDetails
    ) {

        Transaction transaction = getById(id);

        Long departmentId = getAccountDepartmentId(transaction.getAccountTarget().getId());

        requireHasDepartmentAuditAccess(departmentId, userDetails);

        return mapper.toResponse(transaction);

    }

    @Transactional
    public TransactionResponse create(
            @Valid TransactionCreateRequest request,
            CustomUserDetails userDetails
    ) {

        Transaction transaction = mapper.toEntity(request);

        transaction.setStatus(TransactionStatus.DRAFT);

        handleAccounts(
                transaction,
                request.accountTargetId(),
                request.accountFromId(),
                userDetails
        );

        handleCategory(
                transaction,
                request.categoryId()
        );

        handleCounterparty(
                transaction,
                request.counterpartyId()
        );

        handleAuthor(
                transaction,
                userDetails.getId()
        );

        repository.flush();

        return mapper.toResponse(transaction);

    }

    @Transactional
    public TransactionResponse update(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID транзакции")
            Long id,
            @Valid
            TransactionUpdateRequest request,
            CustomUserDetails userDetails
    ) {

        Transaction transaction = getById(id);

        requireHasTransactionModifyAccess(transaction, userDetails);

        requireDraftTransaction(transaction);

        mapper.patch(transaction, request);

        handleAccounts(
                transaction,
                request.accountTargetId(),
                request.accountFromId(),
                userDetails
        );

        handleCategory(
                transaction,
                request.categoryId()
        );

        handleCounterparty(
                transaction,
                request.counterpartyId()
        );

        repository.flush();

        return mapper.toResponse(transaction);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void confirmById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID транзакции")
            Long id,
            CustomUserDetails userDetails
    ) {

        Transaction transaction = getById(id);

        requireHasTransactionModifyAccess(transaction, userDetails);

        requireDraftTransaction(transaction);

        processTransaction(transaction, false);

        transaction.setStatus(TransactionStatus.CONFIRMED);

        transaction.setProcessedAt(OffsetDateTime.now(clock));

        repository.flush();

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void reversalById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID транзакции")
            Long id,
            CustomUserDetails userDetails
    ) {

        Transaction transaction = getById(id);

        requireHasTransactionModifyAccess(transaction, userDetails);

        requireConfirmedTransaction(transaction);

        Transaction canceled = mapper.clone(transaction);

        processTransaction(canceled, true);

        transaction.setStatus(TransactionStatus.REVERSED);

        canceled.setReferenceTransaction(
                repository.getReferenceById(transaction.getId())
        );

        transaction.setProcessedAt(OffsetDateTime.now(clock));

        repository.flush();

    }

    @Transactional
    public void deleteById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID транзакции")
            Long id,
            CustomUserDetails userDetails
    ) {

        Transaction transaction = getById(id);

        requireHasTransactionModifyAccess(transaction, userDetails);

        requireDraftTransaction(transaction);

        repository.deleteById(id);

    }

    private Transaction getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Транзакция с ID " + id + " не найдена"));

    }

    private Long getAccountDepartmentId(Long id) {

        return accountRepository.findById(id)
                .map(Account::getDepartment)
                .map(Department::getId)
                .orElseThrow(() -> new EntityNotFoundException("Счет с ID " + id + " не найден"));

    }

    private void requireCategoryExists(Long id) {

        if (!categoryRepository.existsById(id))
            throw new EntityNotFoundException("Категория с ID " + id + " не найдена");

    }

    private void requireCounterpartyExists(Long id) {

        if (!counterpartyRepository.existsById(id))
            throw new EntityNotFoundException("Контрагент с ID " + id + " не найден");

    }

    private void handleAccounts(
            Transaction transaction,
            Long accountTargetId,
            Long accountFromId,
            CustomUserDetails userDetails
    ) {

        Long accountTargetDepartmentId = getAccountDepartmentId(accountTargetId);

        requireHasDepartmentTransactionsAccess(
                accountTargetDepartmentId,
                userDetails
        );

        transaction.setAccountTarget(
                accountRepository.getReferenceById(accountTargetId)
        );

        if (TransactionType.TRANSFER.equals(transaction.getType())) {

            Long accountFromDepartmentId = getAccountDepartmentId(accountFromId);

            if (!accountTargetDepartmentId.equals(accountFromDepartmentId))
                throw new IllegalArgumentException("Переводы возможны только в рамках одного подразделения");

            transaction.setAccountFrom(
                    accountRepository.getReferenceById(accountFromId)
            );

        }

    }

    private void handleCategory(
            Transaction transaction,
            Long categoryId
    ) {

        requireCategoryExists(categoryId);

        transaction.setCategory(
                categoryRepository.getReferenceById(categoryId)
        );

    }

    private void handleCounterparty(Transaction transaction, Long counterpartyId) {

        if (counterpartyId != null) {

            requireCounterpartyExists(counterpartyId);

            transaction.setCounterparty(
                    counterpartyRepository.getReferenceById(counterpartyId)
            );

        }

    }

    private void handleAuthor(Transaction transaction, Long userId) {

        transaction.setAuthor(
                userRepository.getReferenceById(userId)
        );

    }

    private void requireHasDepartmentTransactionsAccess(Long departmentId, CustomUserDetails userDetails) {

        if (UserRole.ADMIN.equals(userDetails.getRole()))
            return;

        Long userDepartmentId = userRepository.findById(userDetails.getId())
                .map(User::getDepartment)
                .map(Department::getId)
                .orElseThrow(() -> new EntityNotFoundException("Пользовватель с ID " + userDetails.getId() + " не найден"));

        if (!userDepartmentId.equals(departmentId))
            throw new AccessDeniedException("Отсутствуют права доступа к операции");

    }

    private void requireHasDepartmentAuditAccess(Long departmentId, CustomUserDetails userDetails) {

        if (UserRole.AUDITOR.equals(userDetails.getRole()))
            return;

        requireHasDepartmentTransactionsAccess(departmentId, userDetails);

    }

    private void requireDraftTransaction(Transaction transaction) {

        if (!TransactionStatus.DRAFT.equals(transaction.getStatus()))
            throw new IllegalStateException("Невозможно подтвердить операцию");

    }

    private void requireConfirmedTransaction(Transaction transaction) {

        if (!TransactionStatus.CONFIRMED.equals(transaction.getStatus()))
            throw new IllegalStateException("Невозможно сторнировать операцию");

    }

    private void requireHasTransactionModifyAccess(Transaction transaction, CustomUserDetails userDetails) {

        boolean isAdmin = UserRole.ADMIN.equals(userDetails.getRole());

        boolean isTransactionAuthor = transaction.getAuthor().getId().equals(userDetails.getId());

        if (!isTransactionAuthor || !isAdmin)
            throw new AccessDeniedException("У вас нет доступа к данной операции");

    }

    private void processTransaction(Transaction transaction, boolean isCancel) {

        switch (transaction.getType()) {

            case TransactionType type

                    when TransactionType.TRANSFER.equals(type) && isCancel ->

                    handleCancelTransfer(transaction);

            case TRANSFER -> handleTransfer(transaction);

            case TransactionType type

                    when TransactionType.INCOME.equals(type) && isCancel ->

                    handleExpense(
                            transaction.getAccountTarget(),
                            transaction.getAmount(),
                            false
                    );

            case INCOME -> handleIncome(
                    transaction.getAccountTarget(),
                    transaction.getAmount()
            );

            case TransactionType type

                    when TransactionType.EXPENSE.equals(type) && isCancel ->

                    handleIncome(
                            transaction.getAccountTarget(),
                            transaction.getAmount()
                    );

            case EXPENSE -> handleExpense(
                    transaction.getAccountTarget(),
                    transaction.getAmount(),
                    true
            );

            default -> throw new UnsupportedOperationException(
                    "Операция " + transaction.getType().name() + " не поддерживается");

        }

    }

    private void handleTransfer(Transaction transaction) {

        if (transaction.getAccountFrom() == null)
            throw new IllegalStateException("Невозможно выполнить перевод. Не указан счет списания");

        handleExpense(transaction.getAccountFrom(), transaction.getAmount(), true);

        handleIncome(transaction.getAccountTarget(), transaction.getAmount());

    }

    private void handleCancelTransfer(Transaction transaction) {

        if (transaction.getAccountFrom() == null)
            throw new IllegalStateException("Невозможно выполнить перевод. Не указан счет списания");

        handleIncome(transaction.getAccountFrom(), transaction.getAmount());

        handleExpense(transaction.getAccountTarget(), transaction.getAmount(), false);

    }

    private void handleExpense(Account account, BigDecimal amount, boolean checkBalance) {

        if (checkBalance && account.getBalance().compareTo(amount) < 0)
            throw new NotEnoughFundsException("Недостаточно средств на счету");

        addToBalance(
                account,
                amount.negate()
        );

    }

    private void handleIncome(Account account, BigDecimal amount) {

        addToBalance(account, amount);

    }

    private void addToBalance(Account account, BigDecimal amount) {

        BigDecimal newBalance = account.getBalance().add(amount);

        account.setBalance(newBalance);

    }
}
