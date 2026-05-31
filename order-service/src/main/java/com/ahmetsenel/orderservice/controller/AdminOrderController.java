package com.ahmetsenel.orderservice.controller;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.orderservice.entity.Order;
import com.ahmetsenel.orderservice.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable Long id){
        return ApiResponse.success(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ApiResponse.success(orderService.getOrdersByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ApiResponse.success("Order deleted successfully");
    }
}
