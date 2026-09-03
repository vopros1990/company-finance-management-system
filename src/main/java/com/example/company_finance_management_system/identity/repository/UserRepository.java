package com.example.company_finance_management_system.identity.repository;

import com.example.company_finance_management_system.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"department"})
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    Page<User> findByDepartmentId(Long departmentId, Pageable pageable);

}
