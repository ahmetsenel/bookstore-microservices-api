package com.ahmetsenel.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookResponse {

    private Long id;

    private String title;

    private BigDecimal price;

    private Integer stock;
}
