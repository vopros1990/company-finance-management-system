package com.example.company_finance_management_system.identity.repository;

import com.example.company_finance_management_system.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
