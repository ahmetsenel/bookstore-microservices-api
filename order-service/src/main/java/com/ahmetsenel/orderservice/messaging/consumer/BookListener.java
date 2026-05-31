package com.ahmetsenel.orderservice.messaging.consumer;

import com.ahmetsenel.common.event.StockResultEvent;
import com.ahmetsenel.orderservice.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookListener {

    private final OrderService orderService;

    @KafkaListener(topics = "stock-result", groupId = "order-group")
    public void handleStockResult(StockResultEvent event) {
        orderService.updateStatus(event);
    }
}
