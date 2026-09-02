package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.common.exception.InvalidSessionException;
import com.example.company_finance_management_system.configuration.AppSecurityConfigurationProperties;
import com.example.company_finance_management_system.identity.entity.Session;
import com.example.company_finance_management_system.identity.mapping.SessionMapper;
import com.example.company_finance_management_system.identity.repository.SessionRepository;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.utils.HashUtils;
import com.example.company_finance_management_system.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final AppSecurityConfigurationProperties securityConfiguration;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper mapper;
    private final Clock clock;

    public SessionContext createSession(Long userId) {

        String refreshToken = TokenUtils.generateSecureToken();

        Session session = sessionRepository.save(
                buildSession(userId, refreshToken)
        );

        return new SessionContext(refreshToken, session.getId(), userId);

    }

    @Transactional
    public SessionContext updateSession(String refreshToken) {

        Session session = sessionRepository.findByRefreshTokenHash(HashUtils.sha256hash(refreshToken))
                .orElseThrow(() -> new IllegalStateException("Сессия не найдена"));

        requireValidSession(session);

        String newRefreshToken = TokenUtils.generateSecureToken();

        mapper.patch(
                session,
                buildSession(session.getUser().getId(), newRefreshToken)
        );

        sessionRepository.save(session);

        return new SessionContext(newRefreshToken, session.getId(), session.getUser().getId());

    }

    @Transactional
    public void revokeSession(String refreshToken) {

        Session session = sessionRepository.findByRefreshTokenHash(HashUtils.sha256hash(refreshToken))
                .orElseThrow(() -> new IllegalStateException("Сессия не найдена"));

        session.setRevoked(true);

        sessionRepository.save(session);

    }

    public void requireValidSession(Long userId, Long sessionId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalStateException("Сессия с ID " + sessionId + " не найдена"));

        if (!session.getUser().getId().equals(userId))
            throw new InvalidSessionException("Неверная сессия");

        requireValidSession(session);

    }

    public void requireValidSession(Session session) {

        requireSessionNotExpired(session);

        requireSessionNotRevoked(session);

    }

    private void requireSessionNotRevoked(Session session) {

        if (session.getRevoked())
            throw new InvalidSessionException("Сессия отозвана");

    }

    private void requireSessionNotExpired(Session session) {

        OffsetDateTime now = OffsetDateTime.now(clock);

        if (session.getExpiresAt().isBefore(now))
            throw new InvalidSessionException("Сессия просрочена");

    }

    private Session buildSession(Long userId, String refreshToken) {

        OffsetDateTime now = OffsetDateTime.now(clock);

        return Session.builder()
                .user(userRepository.getReferenceById(userId))
                .revoked(false)
                .createdAt(now)
                .expiresAt(now.plus(securityConfiguration.refreshTokenExpiry()))
                .refreshTokenHash(HashUtils.sha256hash(refreshToken))
                .build();


    }

}
