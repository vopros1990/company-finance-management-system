package com.example.company_finance_management_system.configuration;

import com.example.company_finance_management_system.identity.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

    private final AppSecurityConfigurationProperties securityProperties;

    private final Clock clock;

    @Bean
    public JwtUtils jwtUtils() {

        return new JwtUtils(
                securityProperties.base64Secret(),
                securityProperties.accessTokenExpiry(),
                clock
        );

    }

}
