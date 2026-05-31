package com.ahmetsenel.bookservice.controller;

import com.ahmetsenel.bookservice.dto.BookDetail;
import com.ahmetsenel.bookservice.dto.BookRequest;
import com.ahmetsenel.bookservice.dto.BookSummary;
import com.ahmetsenel.bookservice.dto.StockUpdateRequest;
import com.ahmetsenel.bookservice.entity.Book;
import com.ahmetsenel.bookservice.service.impl.BookService;
import com.ahmetsenel.common.dto.PaginationRequest;
import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ApiResponse<PageResponse<BookSummary>> getAllBooks(@ModelAttribute PaginationRequest paginationRequest) {
        return ApiResponse.success(bookService.getAllBooks(paginationRequest.getPage(), paginationRequest.getSize()));
    }

    @GetMapping("/{id}")
    public ApiResponse<BookDetail> getBookById(@PathVariable Long id) {
        return ApiResponse.success(bookService.getBookDetailById(id));
    }
}
