package com.example.company_finance_management_system.common.dto.response;

public record ErrorResponse(

        Object message,
        int code

) {

    public static ErrorResponse of(Object message, int code) {

        return new ErrorResponse(message, code);

    }

}
