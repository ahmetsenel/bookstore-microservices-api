package com.ahmetsenel.bookservice.controller;

import com.ahmetsenel.bookservice.dto.BookDetail;
import com.ahmetsenel.bookservice.dto.BookRequest;
import com.ahmetsenel.bookservice.dto.StockUpdateRequest;
import com.ahmetsenel.bookservice.entity.Book;
import com.ahmetsenel.bookservice.service.impl.BookService;
import com.ahmetsenel.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/books")
public class AdminBookController {

    private final BookService bookService;

    @PostMapping
    public ApiResponse<BookDetail> createBook(@RequestBody BookRequest bookRequest) {
        return ApiResponse.success(bookService.createBook(bookRequest));
    }

    @PatchMapping("/{id}/stock")
    public ApiResponse<Book> updateStock(@PathVariable Long id, @RequestBody StockUpdateRequest request) {
        return ApiResponse.success(bookService.updateStock(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ApiResponse.success("Book deleted successfully");
    }
}
