package com.example.company_finance_management_system.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.bootstrap-admin")
public record BootstrapAdminProperties(

        @NotBlank(message = "Укажите email администратора")
        String email,

        @NotBlank(message = "Укажите пароль администратора")
        String password

) {
}