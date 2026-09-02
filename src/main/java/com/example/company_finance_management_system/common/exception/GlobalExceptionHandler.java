package com.example.company_finance_management_system.common.exception;

import com.example.company_finance_management_system.common.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidSessionException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {

        return ErrorResponse.of(e.getMessage(), HttpStatus.UNAUTHORIZED.value());

    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(Exception e) {

        return ErrorResponse.of("Доступ запрещен", HttpStatus.FORBIDDEN.value());

    }

    @ExceptionHandler({
            BadRequestException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(Exception e) {

        return ErrorResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST.value());

    }

    @ExceptionHandler({
            ConstraintViolationException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationExceptionException(ConstraintViolationException e) {

        List<String> messages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();

        return messages.size() == 1 ?
                ErrorResponse.of(messages.getFirst(), HttpStatus.BAD_REQUEST.value()) :
                ErrorResponse.of(messages, HttpStatus.BAD_REQUEST.value());

    }

    @ExceptionHandler({
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(Exception e) {

        return ErrorResponse.of(e.getMessage(), HttpStatus.CONFLICT.value());

    }

}
