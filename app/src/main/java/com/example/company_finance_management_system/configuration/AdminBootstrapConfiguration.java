package com.example.company_finance_management_system.configuration;

import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BootstrapAdminProperties.class)
public class AdminBootstrapConfiguration {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final BootstrapAdminProperties properties;

    @PostConstruct
    public void createAdminSuperuserIfNotExists() {

        if (repository.existsByName("admin"))
            return;

        String password = properties.password();

        String email = properties.email();

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
