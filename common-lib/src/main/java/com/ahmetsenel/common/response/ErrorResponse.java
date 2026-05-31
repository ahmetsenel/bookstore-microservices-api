package com.ahmetsenel.common.response;

import com.ahmetsenel.common.dto.FieldValidationError;
import com.ahmetsenel.common.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    private String path;

    private String error;

    private String message;

    private List<FieldValidationError> validationErrors;

    public static ErrorResponse from(BaseException ex, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus())
                .error(ex.getMessageType().toString())
                .message(ex.getMessage())
                .path(path)
                .build();
    }
}
