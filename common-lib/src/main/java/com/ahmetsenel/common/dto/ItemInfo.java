package com.ahmetsenel.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemInfo {

    private String title;
    private Integer quantity;
    private BigDecimal price;
}