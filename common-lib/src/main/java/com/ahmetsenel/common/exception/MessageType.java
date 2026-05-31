package com.ahmetsenel.common.exception;

import lombok.Getter;

@Getter
public enum MessageType {

    USERNAME_ALREADY_EXIST(1000, 400,"Username already exist"),
    USER_NOT_FOUND(1001, 404, "User not found"),
    BOOK_NOT_FOUND(1002, 404,"Book not found"),
    ORDER_NOT_FOUND(1003, 404,"Order not found"),
    OUT_OF_STOCK(1004, 404, "Out of stock"),
    INVALID_TOKEN(2001, 401,"Invalid token"),
    TOKEN_EXPIRED(2002, 401,"Token expired"),
    MISSING_TOKEN(2003, 404,"Missing token"),
    ACCESS_DENIED(3001, 403, "Access Denied"),
    INTERNAL_SERVER_ERROR(9999, 500, "Internal server error");

    private final Integer code;
    private final Integer status;
    private final String message;

    MessageType(Integer code, Integer status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
