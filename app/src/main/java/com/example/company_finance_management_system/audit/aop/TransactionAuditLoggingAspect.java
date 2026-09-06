package com.example.company_finance_management_system.audit.aop;

import com.example.company_finance_management_system.audit.aop.dto.OperationSummary;
import com.example.company_finance_management_system.finance.api.v1.dto.response.TransactionResponse;
import com.example.company_finance_management_system.audit.entity.OperationType;
import com.example.company_finance_management_system.audit.service.AuditEntryService;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionAuditLoggingAspect {

    private final AuditEntryService service;

    @Pointcut(value = "execution(* *.TransactionService.create(..))")
    public void create() {}

    @Pointcut(value = "execution(* *.TransactionService.update(..))")
    public void update() {}

    @Pointcut(value = "execution(* *.TransactionService.confirmById(..))")
    public void confirm() {}

    @Pointcut(value = "execution(* *.TransactionService.reversalById(..))")
    public void reverse() {}

    @Pointcut(value = "execution(* *.TransactionService.deleteById(..))")
    public void delete() {}

    @Around("create()")
    public void logCreate(ProceedingJoinPoint pjp) throws Throwable {

        log.debug("TransactionService.create() intercepted");

        OperationSummary.OperationSummaryBuilder builder = OperationSummary.builder();

        try {

            TransactionResponse response = (TransactionResponse) pjp.proceed();

            builder
                    .transactionId(response.id())
                    .userId(getCurrentUserId())
                    .operationType(OperationType.CREATE);

        } catch (Throwable e) {

            builder.failed(true);

            throw e;

        } finally {

            service.logTransaction(builder.build());

        }

    }

    @Around(value = "update()")
    public void logUpdate(ProceedingJoinPoint pjp) throws Throwable {

        log.debug("TransactionService.update() intercepted");

        Long transactionId = (Long) pjp.getArgs()[0];

        processLog(
                OperationType.UPDATE,
                transactionId,
                pjp
        );

    }

    @Around(value = "confirm()")
    public void logConfirm(ProceedingJoinPoint pjp) throws Throwable {

        log.debug("TransactionService.confirmById() intercepted");

        Long transactionId = (Long) pjp.getArgs()[0];

        processLog(
                OperationType.CONFIRM,
                transactionId,
                pjp
        );

    }

    @Around(value = "reverse()")
    public void logCancel(ProceedingJoinPoint pjp) throws Throwable {

        log.debug("TransactionService.cancelById() intercepted");

        Long transactionId = (Long) pjp.getArgs()[0];

        processLog(
                OperationType.CANCEL,
                transactionId,
                pjp
        );

    }

    @Around(value = "delete()")
    public void logDelete(ProceedingJoinPoint pjp) throws Throwable {

        log.debug("TransactionService.deleteById() intercepted");

        Long transactionId = (Long) pjp.getArgs()[0];

        processLog(
                OperationType.DELETE,
                transactionId,
                pjp
        );

    }

    private void processLog(OperationType operationType, Long transactionId, ProceedingJoinPoint pjp) throws Throwable {

        OperationSummary.OperationSummaryBuilder builder = OperationSummary.builder();

        try {

            builder
                    .transactionId(transactionId)
                    .userId(getCurrentUserId())
                    .operationType(operationType);

            pjp.proceed();

        } catch (Throwable e) {

            builder.failed(true);

            throw e;

        } finally {

            service.logTransaction(builder.build());

        }

    }

    private Long getCurrentUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof CustomUserDetails user)
            return user.getId();

        return null;

    }

}
