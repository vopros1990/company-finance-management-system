package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccessService {

    private final UserRepository repository;

    public User getByEmail(String email) {

        return repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Пользователь с email " + email + " не найден"
                ));

    }

    public User getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с ID " + id + " не найден"
                ));

    }

}
