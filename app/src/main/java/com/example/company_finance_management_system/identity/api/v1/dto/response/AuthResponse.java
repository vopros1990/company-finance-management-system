package com.example.company_finance_management_system.identity.api.v1.dto.response;

public record AuthResponse(

        String accessToken,
        String refreshToken

) {
}
