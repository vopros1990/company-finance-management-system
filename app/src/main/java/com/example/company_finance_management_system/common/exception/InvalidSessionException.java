package com.example.company_finance_management_system.common.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidSessionException extends AuthenticationException {
    public InvalidSessionException(String message) {
        super(message);
    }
}
