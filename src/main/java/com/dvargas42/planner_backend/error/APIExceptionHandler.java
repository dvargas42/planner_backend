package com.dvargas42.planner_backend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.dvargas42.planner_backend.exception.CustomException;

@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        APIError apiError = new APIError(
            HttpStatus.BAD_REQUEST,
            "Request error",
            ex.getMessage(),
            request);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {

        APIError apiError = new APIError(
            HttpStatus.BAD_REQUEST,
            "Request error",
            ex.getFieldErrors(),
            request);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }
}

