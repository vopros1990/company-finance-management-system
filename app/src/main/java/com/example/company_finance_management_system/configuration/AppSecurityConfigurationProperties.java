package com.example.company_finance_management_system.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security")
public record AppSecurityConfigurationProperties(
        @NotBlank(message = "Добавьте переменную окружения JWT_SECRET с Base64-encoded секретом")
        String base64Secret,

        @DefaultValue("5m")
        Duration accessTokenExpiry,

        @DefaultValue("30d")
        Duration refreshTokenExpiry
) {
}
