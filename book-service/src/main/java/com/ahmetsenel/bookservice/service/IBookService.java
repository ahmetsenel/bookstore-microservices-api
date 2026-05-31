package com.ahmetsenel.bookservice.service;

import com.ahmetsenel.bookservice.dto.BookDetail;
import com.ahmetsenel.bookservice.dto.BookRequest;
import com.ahmetsenel.bookservice.dto.BookSummary;
import com.ahmetsenel.bookservice.dto.StockUpdateRequest;
import com.ahmetsenel.bookservice.entity.Book;
import com.ahmetsenel.common.event.OrderCreatedEvent;
import com.ahmetsenel.common.response.PageResponse;

import java.util.List;

public interface IBookService {

    BookDetail createBook(BookRequest bookRequest);

    PageResponse<BookSummary> getAllBooks(Integer page, Integer size);

    Book getBookById(Long id);

    BookDetail getBookDetailById(Long id);

    Book updateStock(Long bookId, StockUpdateRequest quantity);

    void deleteBook(Long id);

    void decreaseStock(Long id, StockUpdateRequest request);

    void handleOrder(OrderCreatedEvent event);
}
