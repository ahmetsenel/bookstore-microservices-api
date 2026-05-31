package com.ahmetsenel.userservice.handler;

import com.ahmetsenel.common.dto.FieldValidationError;
import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResponse> handleBaseException (BaseException ex, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorResponse.from(ex, path));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {

        String path = request.getDescription(false).substring(4);

        List<FieldValidationError> errors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(err -> new FieldValidationError(
                                err.getField(),
                                err.getDefaultMessage()
                        ))
                        .toList();

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR")
                .message("Validation failed")
                .path(path)
                .validationErrors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}