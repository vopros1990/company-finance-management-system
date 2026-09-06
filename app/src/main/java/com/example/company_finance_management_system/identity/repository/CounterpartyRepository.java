package com.example.company_finance_management_system.identity.repository;

import com.example.company_finance_management_system.identity.entity.Counterparty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CounterpartyRepository extends JpaRepository<Counterparty, Long>, PagingAndSortingRepository<Counterparty, Long> {

}
