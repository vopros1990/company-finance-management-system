package com.example.company_finance_management_system.identity.repository;

import com.example.company_finance_management_system.identity.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByRefreshTokenHash(String refreshTokenHash);

}
