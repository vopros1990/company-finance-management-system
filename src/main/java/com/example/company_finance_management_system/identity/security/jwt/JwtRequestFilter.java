package com.example.company_finance_management_system.identity.security.jwt;

import com.example.company_finance_management_system.identity.entity.Session;
import com.example.company_finance_management_system.identity.security.CustomUserDetailsService;
import com.example.company_finance_management_system.identity.service.SessionAccessService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final SessionAccessService sessionAccessService;

    private final CustomUserDetailsService userDetailsService;

    private final Clock clock;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = extractAuthorizationHeader(request);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            filterChain.doFilter(request, response);

        String token = authorizationHeader.substring(7);

        processAuthenticate(token);

        filterChain.doFilter(request, response);

    }

    private String extractAuthorizationHeader(HttpServletRequest request) {

        return request.getHeader(HttpHeaders.AUTHORIZATION);

    }

    private void processAuthenticate(String token) {

        if (jwtUtils.isValidToken(token))
            return;

        Claims claims = jwtUtils.parseClaims(token);

        Long userId = jwtUtils.extractSubjectLongValue(claims);

        Long sessionId = jwtUtils.extractSessionId(claims);

        if (!isValidSession(userId, sessionId))
            return;

        tryAuthenticate(userId);

    }

    private void tryAuthenticate(Long userId) {

        try {

            UserDetails userDetails = userDetailsService.loadUserById(userId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(userDetails);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {

            log.warn("Failed to authenticate user with ID {}", userId, e);

        }

    }

    private boolean isValidSession(Long userId, Long sessionId) {

        try {

            Session session = sessionAccessService.getById(sessionId);

            OffsetDateTime now = OffsetDateTime.now(clock);

            return !session.getRevoked()
                    && !session.getExpiresAt().isBefore(now)
                    && session.getUser().getId().equals(userId);

        } catch (Exception e) {

            log.warn("Invalid session {}", sessionId, e);

        }

        return false;

    }

}
