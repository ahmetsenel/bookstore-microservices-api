package com.ahmetsenel.orderservice.controller;

import com.ahmetsenel.common.dto.OrderRequest;
import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.orderservice.dto.OrderResponse;
import com.ahmetsenel.orderservice.entity.Order;
import com.ahmetsenel.orderservice.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody OrderRequest orderRequest) {
        return ApiResponse.success(orderService.createOrder(jwt, orderRequest));
    }

    @GetMapping("/me")
    public ApiResponse<List<Order>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.success(orderService.getMyOrders(jwt));
    }
}
