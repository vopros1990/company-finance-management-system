package com.example.company_finance_management_system.identity.security;

import com.example.company_finance_management_system.identity.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final String email;
    @Getter
    private final Long id;
    @Getter
    private final String password;
    @Getter
    private final Set<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {

        this.id = user.getId();
        this.email = user.getEmail();
        this.authorities = Collections.singleton(
                new SimpleGrantedAuthority(
                        user.getRole().name()));
        this.password = user.getPasswordHash();

    }

    @Override
    public String getUsername() {
        return email;
    }

}
