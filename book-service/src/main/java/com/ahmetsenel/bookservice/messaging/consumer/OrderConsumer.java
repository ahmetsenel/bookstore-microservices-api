package com.ahmetsenel.bookservice.messaging.consumer;

import com.ahmetsenel.bookservice.service.impl.BookService;
import com.ahmetsenel.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final BookService stockService;

    @KafkaListener(topics = "order-created", groupId = "book-group")
    public void consume(OrderCreatedEvent event) {
        stockService.handleOrder(event);
    }
}
