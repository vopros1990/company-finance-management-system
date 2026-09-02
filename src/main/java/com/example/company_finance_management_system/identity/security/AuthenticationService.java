package com.example.company_finance_management_system.identity.security;

import com.example.company_finance_management_system.identity.api.v1.dto.request.AuthRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.RefreshRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserRegisterRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.AuthResponse;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserRegisterResponse;
import com.example.company_finance_management_system.identity.service.UserAccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthenticationService {

    private UserAccessService accessService;

    public UserRegisterResponse register(@Valid UserRegisterRequest request) {

        return null;

    }

    public AuthResponse authenticate(@Valid AuthRequest request) {

        return null;

    }

    public void logout(CustomUserDetails userDetails) {



    }

    public AuthResponse refresh(@Valid RefreshRequest request) {

        return null;

    }
}
