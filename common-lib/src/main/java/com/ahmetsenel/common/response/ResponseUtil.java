package com.ahmetsenel.common.response;

public class ResponseUtil {

    public static <T> T unwrap(ApiResponse<T> response) {
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getMessage());
        }
        return response.getData();
    }
}
