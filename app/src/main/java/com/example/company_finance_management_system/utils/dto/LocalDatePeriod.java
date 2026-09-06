package com.example.company_finance_management_system.utils.dto;

import java.time.LocalDate;

public record LocalDatePeriod(
        LocalDate from,
        LocalDate to
) {
}
