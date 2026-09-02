package com.example.company_finance_management_system.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfigurationProperties(
        @NotBlank(message = "Добавьте переменную окружения JWT_SECRET с Base64-encoded секретом")
        String base64Secret,

        @NotNull(message = "Укажите время жизни access-токена в формате Duration (например, 5m): app.security.jwt.access-token-expiry")
        Duration accessTokenExpiry,

        @NotNull(message = "Укажите время жизни refresh-токена в формате Duration (например, 30d): app.security.jwt.refresh-token-expiry")
        Duration refreshTokenExpiry
) {
}
