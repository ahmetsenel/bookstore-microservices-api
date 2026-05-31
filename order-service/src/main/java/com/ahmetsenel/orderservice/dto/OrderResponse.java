package com.ahmetsenel.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private Long userId;

    private String status;

    private BigDecimal totalPrice;

    private List<OrderItemResponse> items;
}
