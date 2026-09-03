package com.example.company_finance_management_system.identity.api.v1.controller;

import com.example.company_finance_management_system.identity.api.v1.dto.jsonview.UserView;
import com.example.company_finance_management_system.identity.api.v1.dto.request.AuthRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.RefreshTokenRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserRegisterRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.AuthResponse;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.security.AuthenticationService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @JsonView(UserView.class)
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.register(request)
                );

    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {

        return ResponseEntity.ok(
                service.authenticate(request)
        );

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {

        service.logout(request);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                service.refresh(request)
        );

    }

}
