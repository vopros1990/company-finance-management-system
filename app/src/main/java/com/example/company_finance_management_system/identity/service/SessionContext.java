package com.example.company_finance_management_system.identity.service;

public record SessionContext(
        String refreshToken,
        Long sessionId,
        Long userId
) {
}
