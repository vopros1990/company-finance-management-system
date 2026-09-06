package com.example.company_finance_management_system.common.exception;

public class JwtTokenValidationException extends RuntimeException {

    public JwtTokenValidationException(String message, Throwable cause) {

        super(message, cause);

    }

}
