package com.ahmetsenel.bookservice.messaging.producer;

import com.ahmetsenel.common.event.StockResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendSuccess(Long orderId) {
        kafkaTemplate.send("stock-result",
                orderId.toString(),
                new StockResultEvent(orderId, true, "OK"));
    }

    public void sendFail(Long orderId, String message) {
        kafkaTemplate.send("stock-result",
                orderId.toString(),
                new StockResultEvent(orderId, false, message));
    }
}
