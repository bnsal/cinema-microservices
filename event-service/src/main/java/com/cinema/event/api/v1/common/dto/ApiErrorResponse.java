package com.cinema.event.api.v1.common.dto;

public record ApiErrorResponse(
        boolean success,
        String code,
        String message
) {

    public static ApiErrorResponse of(String code, String message) {
        return new ApiErrorResponse(false, code, message);
    }
}
