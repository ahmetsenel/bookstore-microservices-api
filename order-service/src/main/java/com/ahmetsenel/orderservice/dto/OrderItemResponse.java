package com.ahmetsenel.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long id;

    private Long BookId;

    private Integer quantity;

    private BigDecimal price;
}
