package com.example.transmagdalena.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        @JsonFormat(shape = JsonFormat.Shape.STRING)OffsetDateTime timeStamp,
        int status, String error, String message,
        String path,
        List<FieldViolation> fieldViolations
        )

{
    public static ApiError of(HttpStatus status, String message, String path,
                              List<FieldViolation> fieldViolations){
        return new ApiError(OffsetDateTime.now(), status.value(), status.getReasonPhrase(),
                message, path, fieldViolations);
    }

    public record FieldViolation(String field, String message){}
}

