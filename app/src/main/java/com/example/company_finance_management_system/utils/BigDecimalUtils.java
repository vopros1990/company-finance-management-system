package com.example.company_finance_management_system.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public final class BigDecimalUtils {

    private final static RoundingMode roundingMode = RoundingMode.HALF_UP;

    public static BigDecimal conversionRatePercents(BigDecimal converted, BigDecimal total) {

        if (converted.signum() == 0 || total.signum() == 0)
            return BigDecimal.ZERO;

        return converted
                .multiply(new BigDecimal("100"))
                .divide(total, 2, roundingMode);

    }


}
