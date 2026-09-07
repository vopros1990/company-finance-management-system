package com.example.company_finance_management_system.identity.api.v1.controller;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.example.company_finance_management_system.identity.api.v1.dto.request.DepartmentCreateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.request.DepartmentUpdateRequest;
import com.example.company_finance_management_system.identity.api.v1.dto.response.DepartmentResponse;
import com.example.company_finance_management_system.identity.service.DepartmentService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    public PagedModel<DepartmentResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @GetMapping("/{departmentId}")
    @JsonView(Extended.class)
    public DepartmentResponse findById(@PathVariable Long departmentId) {

        return service.findById(departmentId);

    }

    @PostMapping
    @JsonView(Extended.class)
    public DepartmentResponse create(@RequestBody DepartmentCreateRequest request) {

        return service.create(request);

    }

    @PatchMapping("/{departmentId}")
    @JsonView(Extended.class)
    public DepartmentResponse update(
            @PathVariable Long departmentId,
            @RequestBody DepartmentUpdateRequest request
    ) {

        return service.update(departmentId, request);

    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long departmentId) {

        service.deleteById(departmentId);

        return ResponseEntity.noContent().build();

    }

}
