package com.ahmetsenel.orderservice.decoder;

import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.exception.MessageType;
import com.ahmetsenel.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {

        try {

            ErrorResponse errorResponse =
                    objectMapper.readValue(
                            response.body().asInputStream(),
                            ErrorResponse.class
                    );

            return new BaseException(MessageType.valueOf(errorResponse.getError()));

        } catch (Exception e) {
            return new BaseException(MessageType.INTERNAL_SERVER_ERROR);
        }
    }
}