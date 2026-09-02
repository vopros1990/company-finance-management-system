package com.example.company_finance_management_system.identity.service;

import com.example.company_finance_management_system.identity.entity.Session;
import com.example.company_finance_management_system.identity.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionAccessService {

    private final SessionRepository repository;

    public Session getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сессия с ID " + id + " не найдена"));

    }

}
