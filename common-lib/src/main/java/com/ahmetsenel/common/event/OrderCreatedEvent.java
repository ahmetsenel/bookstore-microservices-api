package com.ahmetsenel.common.event;

import com.ahmetsenel.common.dto.OrderItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;

    private List<OrderItemRequest> items;
}
