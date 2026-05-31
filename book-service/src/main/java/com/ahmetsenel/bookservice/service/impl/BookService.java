package com.ahmetsenel.bookservice.service.impl;

import com.ahmetsenel.bookservice.dto.*;
import com.ahmetsenel.bookservice.entity.Book;
import com.ahmetsenel.bookservice.mapper.BookMapper;
import com.ahmetsenel.bookservice.messaging.producer.StockResultProducer;
import com.ahmetsenel.bookservice.repository.BookRepository;
import com.ahmetsenel.bookservice.service.IBookService;
import com.ahmetsenel.common.dto.OrderItemRequest;
import com.ahmetsenel.common.event.OrderCreatedEvent;
import com.ahmetsenel.common.exception.BaseException;
import com.ahmetsenel.common.exception.MessageType;
import com.ahmetsenel.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Set<Long> processedOrders = ConcurrentHashMap.newKeySet();
    private final StockResultProducer stockResultProducer;

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.BOOK_NOT_FOUND));
    }

    @Override
    public BookDetail createBook(BookRequest bookRequest) {
        Book book = bookMapper.toEntity(bookRequest);
        bookRepository.save(book);
        return bookMapper.toDetail(book);
    }

    @Override
    public PageResponse<BookSummary> getAllBooks(Integer page, Integer size) {

        Page<BookSummary> books = bookRepository
                .findAll(PageRequest.of(page, size))
                .map(bookMapper::toSummary);
        return PageResponse.from(books);
    }

    @Override
    public BookDetail getBookDetailById(Long id) {
        return bookMapper.toDetail(getBookById(id));
    }

    @Override
    public Book updateStock(Long bookId, StockUpdateRequest request) {
        Book book = getBookById(bookId);
        book.setStock(request.getQuantity());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Transactional
    @Override
    public void decreaseStock(Long id, StockUpdateRequest request) {
        Book book = getBookById(id);
        if (book.getStock() < request.getQuantity()) {
            throw new BaseException(MessageType.OUT_OF_STOCK);
        }
        book.setStock(book.getStock() - request.getQuantity());
    }

    @Transactional
    @Override
    public void handleOrder(OrderCreatedEvent event) {
        if (processedOrders.contains(event.getOrderId())) {
            return;
        }

        try {
            for (OrderItemRequest item : event.getItems()) {

                Book book = getBookById(item.getBookId());

                if (book.getStock() < item.getQuantity()) {
                    throw new BaseException(MessageType.OUT_OF_STOCK);
                }

                book.setStock(book.getStock() - item.getQuantity());
                bookRepository.save(book);
            }

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            processedOrders.add(event.getOrderId());
                            stockResultProducer.sendSuccess(event.getOrderId());
                        }
                    }
            );

        } catch (Exception e) {
            stockResultProducer.sendFail(event.getOrderId(), e.getMessage());
            throw e;
        }
    }
}
