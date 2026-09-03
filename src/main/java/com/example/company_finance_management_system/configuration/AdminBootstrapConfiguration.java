package com.example.company_finance_management_system.configuration;

import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminBootstrapConfiguration {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminSuperuserIfNotExists() {

        if (repository.existsByName("admin"))
            return;

        String password = System.getenv("BOOTSTRAP_ADMIN_PASSWORD");

        String email = System.getenv("BOOTSTRAP_ADMIN_EMAIL");

        if (password == null || email == null)
            throw new IllegalStateException("""
                    Для первого запуска приложения требуется создать admin пользователя.
                    Передайте переменные окружениия BOOTSTRAP_ADMIN_PASSWORD и BOOTSTRAP_ADMIN_EMAIL
                    """);

        User admin = User.builder()
                .name("admin")
                .email(email)
                .role(UserRole.ADMIN)
                .passwordHash(passwordEncoder.encode(password))
                .build();

        repository.save(admin);

        repository.flush();

    }

}
