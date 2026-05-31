package com.ahmetsenel.notificationservice.messaging.consumer;

import com.ahmetsenel.common.event.NotificationEvent;
import com.ahmetsenel.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-completed", groupId = "notification-group")
    public void handleOrderCompleted(NotificationEvent event) throws Exception {
        notificationService.sendHtmlMail(event);
    }
}
