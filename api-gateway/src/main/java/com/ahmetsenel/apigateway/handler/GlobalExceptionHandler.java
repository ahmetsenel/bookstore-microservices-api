package com.ahmetsenel.apigateway.handler;

import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResponse> handleBaseException (BaseException ex, ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorResponse.from(ex, path));
    }
}
