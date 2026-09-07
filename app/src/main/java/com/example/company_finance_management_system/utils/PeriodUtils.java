package com.example.company_finance_management_system.utils;

import com.example.company_finance_management_system.utils.dto.LocalDatePeriod;
import com.example.company_finance_management_system.utils.dto.OffsetDateTimePeriod;
import com.example.company_finance_management_system.utils.dto.PeriodType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneId;

@UtilityClass
public final class PeriodUtils {

    public static LocalDatePeriod resolvePeriod(int targetYear, int periodCount, PeriodType periodType) {

        Year year = Year.parse(String.valueOf(targetYear));

        return switch (periodType) {

            case PeriodType type when PeriodType.QUARTER.equals(type) && periodCount > 4 ->
                    throw new IllegalArgumentException("Номер квартального периода должен быть от 1 до 4");

            case QUARTER ->
                    new LocalDatePeriod(
                            year.atMonth((3 * periodCount) - 2).atDay(1),
                            year.atMonth(3 * periodCount).atEndOfMonth()
                    );


            case MONTH ->
                    new LocalDatePeriod(
                            year.atMonth(periodCount).atDay(1),
                            year.atMonth(periodCount).atEndOfMonth()
                    );

        };

    }

    public static OffsetDateTimePeriod convert(LocalDatePeriod localDatePeriod, ZoneId timeZone) {

        return convert(localDatePeriod.from(), localDatePeriod.to(), timeZone);

    }

    public static OffsetDateTimePeriod convert(LocalDate dateFrom, LocalDate dateTo, ZoneId timeZone) {

        OffsetDateTime from = dateFrom
                .atStartOfDay(timeZone)
                .toOffsetDateTime();

        OffsetDateTime to = dateTo
                .plusDays(1)
                .atStartOfDay(timeZone)
                .minusNanos(1L)
                .toOffsetDateTime();

        return new OffsetDateTimePeriod(from, to);

    }

}
