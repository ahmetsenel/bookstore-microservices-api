package com.ahmetsenel.orderservice.client;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.orderservice.config.FeignConfig;
import com.ahmetsenel.orderservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/internal/users/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable Long id);
}
