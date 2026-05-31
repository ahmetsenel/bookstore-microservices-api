package com.ahmetsenel.common.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException{

    private MessageType messageType;
    private Integer status;

    public BaseException(MessageType messageType) {
        super(messageType.getMessage());
        this.status = messageType.getStatus();
        this.messageType = messageType;
    }

    public BaseException(MessageType messageType,  String message) {
        super(messageType.getMessage() + " " + message);
        this.messageType = messageType;
    }
}
