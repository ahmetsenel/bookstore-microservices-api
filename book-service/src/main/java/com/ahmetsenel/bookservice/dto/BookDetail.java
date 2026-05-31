package com.ahmetsenel.bookservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDetail {

    private Long id;

    private LocalDateTime createdAt;

    private String title;

    private String author;

    private Double price;

    private Integer page;

    private Integer stock;
}
