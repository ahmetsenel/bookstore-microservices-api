package com.ahmetsenel.orderservice.service;

import com.ahmetsenel.common.dto.OrderRequest;
import com.ahmetsenel.common.event.StockResultEvent;
import com.ahmetsenel.orderservice.dto.OrderResponse;
import com.ahmetsenel.orderservice.entity.Order;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(Jwt jwt, OrderRequest orderRequest);

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(Long userId);

    List<Order> getMyOrders(Jwt jwt);

    void updateStatus(StockResultEvent event);

    void deleteOrder(Long id);
}
