package com.ahmetsenel.orderservice.mapper;

import com.ahmetsenel.orderservice.dto.OrderItemResponse;
import com.ahmetsenel.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse toDto(OrderItem item);

    List<OrderItemResponse> toDtoList(List<OrderItem> items);
}
