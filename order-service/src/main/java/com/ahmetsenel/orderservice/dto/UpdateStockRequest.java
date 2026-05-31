package com.ahmetsenel.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateStockRequest {
    private Integer quantity;
}