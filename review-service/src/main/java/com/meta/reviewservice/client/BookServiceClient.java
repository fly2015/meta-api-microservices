package com.meta.reviewservice.client;

import com.meta.reviewservice.client.fallback.BookServiceFallback;
import com.meta.reviewservice.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", fallback= BookServiceFallback.class)
public interface BookServiceClient {
    @GetMapping("/api/v1/books/{bookId}")
    BookDto getBookById(@PathVariable("bookId") Long bookId);
}
