package com.ahmetsenel.notificationservice.service;

import com.ahmetsenel.common.event.NotificationEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendHtmlMail(NotificationEvent event) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(event.getEmail());
        helper.setSubject("Order Details");
        helper.setText(buildMail(event), true);

        mailSender.send(message);
    }


    private String buildMail(NotificationEvent event) {

        StringBuilder sb = new StringBuilder();

        sb.append("<h2>Order ID: ")
                .append(event.getOrderId())
                .append("</h2>");

        sb.append("<ul>");

        event.getItems().forEach(item -> {
            sb.append("<li>")
                    .append(item.getQuantity())
                    .append(" x ")
                    .append(item.getTitle())
                    .append(" x ")
                    .append(item.getPrice())
                    .append("₺</li>");
        });

        sb.append("</ul>");

        sb.append("<b>Total: ")
                .append(event.getTotalPrice())
                .append("₺</b>");

        sb.append("<p><b>Delivery Address:</b> ")
                .append(event.getAddress())
                .append("</p>");

        return sb.toString();
    }
}
