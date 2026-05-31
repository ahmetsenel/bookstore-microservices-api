package com.ahmetsenel.bookservice.controller.internal;

import com.ahmetsenel.bookservice.dto.BookDetail;
import com.ahmetsenel.bookservice.dto.StockUpdateRequest;
import com.ahmetsenel.bookservice.service.impl.BookService;
import com.ahmetsenel.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/books")
public class InternalBookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ApiResponse<BookDetail> getBookById(@PathVariable Long id) {
        return ApiResponse.success(bookService.getBookDetailById(id));
    }

    @PostMapping("/{id}/decrease-stock")
    public void decreaseStock(@PathVariable Long id, @RequestBody StockUpdateRequest request) {
        bookService.decreaseStock(id, request);
    }
}
