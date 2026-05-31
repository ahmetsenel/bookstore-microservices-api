package com.ahmetsenel.bookservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private String title;

    private String author;

    private BigDecimal price;

    private Integer page;

    private Integer stock;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
