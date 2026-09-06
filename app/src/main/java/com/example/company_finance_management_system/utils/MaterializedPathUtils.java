package com.example.company_finance_management_system.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class MaterializedPathUtils {

    public static String merge(String delimiter, String... paths) {

        if (paths == null || paths.length == 0)
            return null;

        StringBuilder builder = new StringBuilder(delimiter);

        for (String path : paths) {

            for (String id : path.split("[^0-9]+")) {

                if(id != null && !id.isBlank())
                    builder.append(id).append(delimiter);

            }

        }

        return builder.toString();

    }

    public static String pathOf(String delimiter, String value) {

        return delimiter + value + delimiter;

    }

}
