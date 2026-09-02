package com.example.company_finance_management_system.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@UtilityClass
@Slf4j
public final class HashUtils {

    public static String sha256hash(String data) {

        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(bytes);

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            log.error(e.getMessage());

            throw new IllegalStateException("Hash algorithm missing");

        }
    }

}
