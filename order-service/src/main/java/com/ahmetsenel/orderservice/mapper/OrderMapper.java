package com.ahmetsenel.orderservice.mapper;

import com.ahmetsenel.orderservice.dto.OrderResponse;
import com.ahmetsenel.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "items", target = "items")
    OrderResponse toDto(Order order);
}
