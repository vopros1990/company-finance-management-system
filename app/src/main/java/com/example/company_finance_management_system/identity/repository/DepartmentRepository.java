package com.example.company_finance_management_system.identity.repository;

import com.example.company_finance_management_system.identity.entity.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long>, PagingAndSortingRepository<Department, Long> {

    @EntityGraph(attributePaths = {"responsibleUser"})
    Optional<Department> findById(Long id);

}
