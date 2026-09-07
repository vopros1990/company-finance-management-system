package com.example.company_finance_management_system.finance.repository;

import com.example.company_finance_management_system.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, PagingAndSortingRepository<Account, Long> {
}
