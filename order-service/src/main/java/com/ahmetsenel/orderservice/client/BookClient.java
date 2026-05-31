package com.ahmetsenel.orderservice.client;

import com.ahmetsenel.common.response.ApiResponse;
import com.ahmetsenel.orderservice.config.FeignConfig;
import com.ahmetsenel.orderservice.dto.BookResponse;
import com.ahmetsenel.orderservice.dto.UpdateStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "book-service", configuration = FeignConfig.class)
public interface BookClient {

    @GetMapping("/internal/books/{id}")
    ApiResponse<BookResponse> getBook(@PathVariable Long id);

    @PostMapping("/internal/books/{id}/decrease-stock")
    void decreaseStock(@PathVariable Long id,
                       @RequestBody UpdateStockRequest request);
}
