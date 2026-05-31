package com.ahmetsenel.bookservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequest {

    private String title;

    private String author;

    private BigDecimal price;

    private Integer page;

    private Integer stock;
}
