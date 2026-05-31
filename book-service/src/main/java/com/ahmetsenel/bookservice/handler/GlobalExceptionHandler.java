package com.ahmetsenel.bookservice.handler;

import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResponse> handleBaseException (BaseException ex, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorResponse.from(ex, path));
    }
}
