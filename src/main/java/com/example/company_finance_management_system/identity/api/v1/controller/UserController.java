package com.example.company_finance_management_system.identity.api.v1.controller;

import com.example.company_finance_management_system.identity.api.v1.dto.jsonview.UserView;
import com.example.company_finance_management_system.identity.api.v1.dto.request.UserUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.UserResponse;
import com.example.company_finance_management_system.identity.security.CustomUserDetails;
import com.example.company_finance_management_system.identity.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService service;

    @GetMapping("/users/me")
    @JsonView(UserView.Extended.class)
    public UserResponse meUser(@AuthenticationPrincipal CustomUserDetails user) {

        return service.findById(user.getId());

    }

    @PutMapping("/users/me")
    @JsonView(UserView.Extended.class)
    public UserResponse updateMeUser(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UserUpdateRequest request
    ) {

        return service.updateUser(user.getId(), request);

    }

    @PutMapping("/users/{userId}")
    @JsonView(UserView.Extended.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponse updateUserById(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest request
    ) {

        return service.updateUser(userId, request);

    }

    @GetMapping("/users/{userId}")
    @JsonView(UserView.Extended.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponse findUser(@PathVariable Long userId) {

        return service.findById(userId);

    }

    @GetMapping("/users")
    @JsonView(UserView.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PagedModel<UserResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @DeleteMapping("/users/{userId}")
    @JsonView(UserView.Extended.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        service.deleteById(userId);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/departments/{departmentId}/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FINANCE_MANAGER')")
    @JsonView(UserView.class)
    public PagedModel<UserResponse> findUsersByDepartment(@PathVariable Long departmentId, Pageable pageable) {

        return service.findByDepartment(departmentId, pageable);

    }

}
