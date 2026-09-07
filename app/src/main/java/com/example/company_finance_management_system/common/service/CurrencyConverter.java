package com.example.company_finance_management_system.common.service;

import com.example.company_finance_management_system.finance.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyConverter {

    // Класс-заглушка для конвертации валюты.
    // Так как конвертация и курсы валют выходят за рамки тестового задания,
    // при попытке конвертировать разные валюты будет выброшено исключение
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {

        if (from.equals(to))
            return amount;

        throw new NotImplementedException("Currency conversion of different types not yet implemented");

    }

}
