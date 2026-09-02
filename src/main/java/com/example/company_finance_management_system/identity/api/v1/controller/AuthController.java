package com.example.company_finance_management_system.identity.api.v1.controller;

import com.example.company_finance_management_system.identity.api.v1.dto.request.AuthRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.RefreshRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserRegisterRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.AuthResponse;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserRegisterResponse;
import com.example.company_finance_management_system.identity.security.AuthenticationService;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.register(request));

    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {

        return ResponseEntity.ok(
                service.authenticate(request)
        );

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {

        return ResponseEntity.ok(
                service.refresh(request)
        );

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {

        service.logout(userDetails);

        return ResponseEntity.ok().build();

    }

}
