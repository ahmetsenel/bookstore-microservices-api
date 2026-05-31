package com.ahmetsenel.bookservice.dto;

import lombok.Data;


@Data
public class BookSummary {
    private Long id;

    private String title;

    private String author;

    private Integer price;
}
