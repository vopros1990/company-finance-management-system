package com.example.company_finance_management_system.identity.security.jwt;

import com.example.company_finance_management_system.common.exception.InvalidSessionException;
import com.example.company_finance_management_system.common.exception.JwtTokenValidationException;
import com.example.company_finance_management_system.identity.security.CustomUserDetailsService;
import com.example.company_finance_management_system.identity.service.SessionService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final SessionService sessionService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = extractAuthorizationHeader(request);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
            
        }

        String token = authorizationHeader.substring(7);

        authenticate(token);

        filterChain.doFilter(request, response);

    }

    private String extractAuthorizationHeader(HttpServletRequest request) {

        return request.getHeader(HttpHeaders.AUTHORIZATION);

    }

    private void authenticate(String token) {

        try {

            jwtUtils.requireValidToken(token);

            Claims claims = jwtUtils.parseClaims(token);

            Long userId = jwtUtils.extractSubjectLongValue(claims);

            Long sessionId = jwtUtils.extractSessionId(claims);

            sessionService.requireValidSession(userId, sessionId);

            tryAuthenticate(userId);

        } catch (JwtTokenValidationException e) {

            log.warn("Invalid token", e);

        } catch (InvalidSessionException e) {

            log.warn("Invalid/revoked session", e);

        } catch (IllegalStateException e) {

            log.warn("Invalid session state", e);

        }

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

}
