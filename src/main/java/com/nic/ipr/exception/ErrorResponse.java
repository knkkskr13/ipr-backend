package com.nic.ipr.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message
) {
    // static factory method for easy creation
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(
                LocalDateTime.now().toString(),
                status,
                error,
                message
        );
    }
}