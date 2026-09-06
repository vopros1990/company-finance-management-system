package com.example.company_finance_management_system.finance.api.v1.controller;

import com.example.company_finance_management_system.common.dto.jsonview.Extended;
import com.example.company_finance_management_system.finance.api.v1.dto.request.CategoryCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.CategoryUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.CategoryResponse;
import com.example.company_finance_management_system.finance.service.CategoryService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public PagedModel<CategoryResponse> findAll(Pageable pageable) {

        return service.findAll(pageable);

    }

    @GetMapping("/{categoryId}")
    @JsonView(Extended.class)
    public CategoryResponse findById(@PathVariable Long categoryId) {

        return service.findById(categoryId);

    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryResponse create(@RequestBody CategoryCreateRequest request) {

        return service.create(request);

    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryResponse update(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequest request) {

        return service.update(categoryId, request);

    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long categoryId) {

        service.deleteById(categoryId);

        return ResponseEntity.noContent().build();

    }

}
