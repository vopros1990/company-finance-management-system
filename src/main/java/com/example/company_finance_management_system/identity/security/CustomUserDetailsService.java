package com.example.company_finance_management_system.identity.security;

import com.example.company_finance_management_system.identity.entity.User;
import com.example.company_finance_management_system.identity.repository.UserRepository;
import com.example.company_finance_management_system.identity.service.UserAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    private final UserAccessService accessService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email " + username + " не найден"));

        return new CustomUserDetails(user);

    }

    public UserDetails loadUserById(Long id) {

        User user = accessService.getById(id);

        return new CustomUserDetails(user);

    }
}
