package com.example.company_finance_management_system.utils.dto;

import java.time.OffsetDateTime;

public record OffsetDateTimePeriod(
        OffsetDateTime from,
        OffsetDateTime to
) {
}
