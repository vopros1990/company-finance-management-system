package com.example.company_finance_management_system.identity.security;

import com.example.company_finance_management_system.identity.api.v1.dto.request.AuthRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.RefreshTokenRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserRegisterRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.AuthResponse;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.mapping.UserMapper;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.security.jwt.JwtUtils;
import com.example.company_finance_management_system.identity.service.SessionContext;
import com.example.company_finance_management_system.identity.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(@Valid UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.email()))
            throw new IllegalStateException("Пользователь с данным email уже зарегистрирован");

        User user = userMapper.toEntity(
                request,
                passwordEncoder.encode(request.password())
        );

        return userMapper.toResponse(
                userRepository.save(user)
        );

    }

    @Transactional
    public AuthResponse authenticate(@Valid AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null)
            throw new AccessDeniedException("Пользователь не аутентифицирован");

        SessionContext session = sessionService.createSession(userDetails.getId());

        String accessToken = jwtUtils.accessToken(
                userDetails.getId().toString(),
                session.sessionId()
        );

        return new AuthResponse(accessToken, session.refreshToken());

    }

    public void logout(@Valid RefreshTokenRequest request) {

        sessionService.revokeSession(request.refreshToken());

    }

    @Transactional
    public AuthResponse refresh(@Valid RefreshTokenRequest request) {

        SessionContext session = sessionService.updateSession(request.refreshToken());

        String accessToken = jwtUtils.accessToken(
                session.userId().toString(),
                session.sessionId()
        );

        return new AuthResponse(accessToken, session.refreshToken());

    }

}
