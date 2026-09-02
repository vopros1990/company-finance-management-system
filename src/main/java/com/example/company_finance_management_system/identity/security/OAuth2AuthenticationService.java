package com.example.company_finance_management_system.identity.security;

import com.example.company_finance_management_system.identity.api.v1.dto.response.AuthResponse;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.entity.UserRole;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.jwt.JwtUtils;
import com.example.company_finance_management_system.identity.service.SessionContext;
import com.example.company_finance_management_system.identity.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final JwtUtils jwtUtils;

    public AuthResponse authenticateByOidc(OidcUser oidcUser) {

        User user = userRepository.findByEmail(oidcUser.getEmail())
                .orElseGet(() -> registerNewOidcUser(oidcUser));

        SessionContext session = sessionService.createSession(user.getId());

        String accessToken = jwtUtils.accessToken(
                user.getId().toString(),
                session.sessionId()
        );

        return new AuthResponse(accessToken, session.refreshToken());

    }

    private User registerNewOidcUser(OidcUser oidcUser) {

        return userRepository.save(
                User.builder()
                        .role(UserRole.AUDITOR)
                        .name(oidcUser.getName())
                        .email(oidcUser.getEmail())
                        .passwordHash(null)
                        .build()
        );

    }

}
