package com.ahmetsenel.orderservice.messaging.producer;

import com.ahmetsenel.common.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCompletedEvent(NotificationEvent event) {
        kafkaTemplate.send("order-completed", event.getOrderId().toString(), event);
    }
}
