package com.example.company_finance_management_system.identity.security.jwt;

import com.example.company_finance_management_system.common.exception.JwtTokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    private final SecretKey secretKey;

    private final Duration accessTokenExpiry;

    private final Clock clock;

    public JwtUtils(
            String base64Secret,
            Duration accessTokenExpiry,
            Clock clock
    ) {

        this.secretKey = generateSecretKey(base64Secret);

        this.accessTokenExpiry = accessTokenExpiry;

        this.clock = clock;

    }

    public String accessToken(String subject, Long sessionId) {

        Instant now = Instant.now(clock);

        Instant expiry = now.plus(accessTokenExpiry);

        return Jwts.builder()
                .subject(subject)
                .claim("sid", sessionId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();

    }

    public Claims parseClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public Long extractSubjectLongValue(Claims claims) {

        return Long.valueOf(claims.getSubject());

    }

    public Long extractSessionId(Claims claims) {

        return claims.get("sid", Long.class);

    }

    public void requireValidToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

        } catch (JwtException | IllegalArgumentException e) {

            throw new JwtTokenValidationException("Невалидный/просроченный токен", e);

        }

    }

    private SecretKey generateSecretKey(String base64Secret) {

        byte[] decoded = Base64.getDecoder().decode(base64Secret);

        return Keys.hmacShaKeyFor(decoded);

    }

}
