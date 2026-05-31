package com.ahmetsenel.orderservice.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.ahmetsenel.common.event.OrderCreatedEvent;

@Component
@RequiredArgsConstructor
public class StockProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created", event.getOrderId().toString(), event);
    }
}
