package com.example.company_finance_management_system.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public final class TokenUtils {

    public static String generateSecureToken() {

        SecureRandom random = new SecureRandom();

        byte[] bytes = new byte[32];

        random.nextBytes(bytes);

        return Base64.getEncoder().encodeToString(bytes);

    }

}
