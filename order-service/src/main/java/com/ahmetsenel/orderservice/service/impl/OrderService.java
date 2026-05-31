package com.ahmetsenel.orderservice.service.impl;

import com.ahmetsenel.common.dto.ItemInfo;
import com.ahmetsenel.common.dto.OrderItemRequest;
import com.ahmetsenel.common.event.NotificationEvent;
import com.ahmetsenel.common.event.StockResultEvent;
import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.exception.MessageType;
import com.ahmetsenel.common.response.ResponseUtil;
import com.ahmetsenel.orderservice.client.BookClient;
import com.ahmetsenel.orderservice.client.UserClient;
import com.ahmetsenel.orderservice.dto.*;
import com.ahmetsenel.orderservice.entity.Order;
import com.ahmetsenel.orderservice.entity.OrderItem;
import com.ahmetsenel.orderservice.mapper.OrderMapper;
import com.ahmetsenel.orderservice.messaging.producer.NotificationProducer;
import com.ahmetsenel.orderservice.messaging.producer.StockProducer;
import com.ahmetsenel.orderservice.repository.OrderRepository;
import com.ahmetsenel.orderservice.service.IOrderService;
import com.ahmetsenel.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.ahmetsenel.common.event.OrderCreatedEvent;
import com.ahmetsenel.common.dto.OrderRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final BookClient bookClient;
    private final OrderMapper orderMapper;
    private final StockProducer stockProducer;
    private final UserClient userClient;
    private final NotificationProducer notificationProducer;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public OrderResponse createOrder(Jwt jwt, OrderRequest orderRequest) {

        Long userId = jwtUtil.getUserIdByToken(jwt.getTokenValue());

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");

        List<OrderItem> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;


        for (OrderItemRequest item : orderRequest.getItems()) {
            BookResponse book = ResponseUtil.unwrap(bookClient.getBook(item.getBookId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(item.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(book.getPrice());
            orderItem.setOrder(order);

            items.add(orderItem);

            totalPrice = totalPrice.add(
                    book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        order.setItems(items);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        stockProducer.sendOrderCreatedEvent(
                                new OrderCreatedEvent(order.getId(), orderRequest.getItems())
                        );
                    }
                });

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(MessageType.ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getMyOrders(Jwt jwt) {
        Long userId = jwtUtil.getUserIdByToken(jwt.getTokenValue());
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    @Transactional
    @Override
    public void updateStatus(StockResultEvent event) {
        Order order = getOrderById(event.getOrderId());

        if (!order.getStatus().equals("PENDING")) {
            return;
        }

        if (event.isSuccess()) {
            order.setStatus("COMPLETED");
            UserResponse user = ResponseUtil.unwrap(userClient.getUser(order.getUserId()));

            NotificationEvent notificationEvent = new NotificationEvent();
            notificationEvent.setOrderId(order.getId());
            notificationEvent.setEmail(user.getEmail());
            notificationEvent.setAddress(user.getAddress());
            notificationEvent.setTotalPrice(order.getTotalPrice());

            List<ItemInfo> itemInfoList = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                BookResponse book = ResponseUtil.unwrap(bookClient.getBook(item.getBookId()));

                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setTitle(book.getTitle());
                itemInfo.setQuantity(item.getQuantity());
                itemInfo.setPrice(item.getPrice());
                itemInfoList.add(itemInfo);
            }
            notificationEvent.setItems(itemInfoList);

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            notificationProducer.sendOrderCompletedEvent(notificationEvent);
                        }
                    });

        } else {
            order.setStatus("FAILED");
        }
    }
}