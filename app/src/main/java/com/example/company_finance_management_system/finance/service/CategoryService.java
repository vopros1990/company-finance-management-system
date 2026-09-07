package com.example.company_finance_management_system.finance.service;

import com.example.company_finance_management_system.finance.mapping.CategoryMapper;
import com.example.company_finance_management_system.finance.api.v1.dto.request.CategoryCreateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.request.CategoryUpdateRequest;
import com.example.company_finance_management_system.finance.api.v1.dto.response.CategoryResponse;
import com.example.company_finance_management_system.finance.entity.Category;
import com.example.company_finance_management_system.finance.repository.CategoryRepository;
import com.example.company_finance_management_system.utils.MaterializedPathUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final static String DELIMITER = "/";

    public PagedModel<CategoryResponse> findAll(Pageable pageable) {

        return new PagedModel<> (
                repository.findAll(pageable)
                        .map(mapper::toResponse)
        );

    }

    @Transactional
    public CategoryResponse findById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID категории") Long id
    ) {

        return mapper.toResponse(
                getById(id)
        );

    }

    @Transactional
    public CategoryResponse create(@Valid CategoryCreateRequest request) {

        Category category = Category.builder()
                .name(request.name())
                .build();

        category = updateCategoryHierarchy(category, request.parentCategoryId());

        return mapper.toResponse(category);

    }

    @Transactional
    public CategoryResponse update(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID категории")
            Long id,
            @Valid CategoryUpdateRequest request
    ) {

        Category category = getById(id);

        category.setName(request.name());

        category = updateCategoryHierarchy(category, request.parentCategoryId());

        updateSubCategoriesHierarchy(repository.findChildren(id), category.getPath());

        repository.flush();

        return mapper.toResponse(category);

    }

    @Transactional
    public void deleteById(
            @Valid
            @Min(value = 1, message = "Укажите корректный ID категории") Long id
    ) {

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Категория с ID " + id + " не найдена");

        repository.deleteById(id);

    }

    private Category getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + id + " не найдена"));

    }

    private Category updateCategoryHierarchy(Category category, Long parentId) {

        Category parent = resolveParent(parentId);

        String parentPath = getParentPath(parent);

        category.setParent(parent);

        if (category.getId() == null)
            category = repository.save(category);

        repository.flush();

        String path = resolvePath(category.getId(), parentId, parentPath);

        category.setPath(path);

        return repository.save(category);

    }

    private void updateSubCategoriesHierarchy(List<Category> children, String parentPath) {

        if (children.isEmpty())
            return;

        for (Category child : children) {

            String path = MaterializedPathUtils.merge(DELIMITER, parentPath, child.getId().toString());

            child.setPath(path);

            updateSubCategoriesHierarchy(repository.findChildren(child.getId()), path);

        }

    }

    private Category resolveParent(Long parentId) {

        if (parentId == null)
            return null;

        return getById(parentId);

    }

    private String getParentPath(Category parent) {

        if (parent == null)
            return "";

        return parent.getPath();

    }

    private String resolvePath(Long categoryId, Long parentId, String parentPath) {

        if (parentId == null)
            return MaterializedPathUtils.pathOf(DELIMITER, categoryId.toString());

        return MaterializedPathUtils.merge(DELIMITER, parentPath, categoryId.toString());

    }

}
