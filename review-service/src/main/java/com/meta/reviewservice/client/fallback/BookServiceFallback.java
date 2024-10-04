package com.meta.reviewservice.client.fallback;

import com.meta.reviewservice.client.BookServiceClient;
import com.meta.reviewservice.dto.BookDto;
import org.springframework.stereotype.Component;

@Component
public class BookServiceFallback implements BookServiceClient {

    @Override
    public BookDto getBookById(Long bookId) {
        // Fallback logic: return a default BookDto or handle the failure gracefully
        BookDto fallbackBook = new BookDto();
        fallbackBook.setId(bookId);
        fallbackBook.setTitle("Default Title");
        fallbackBook.setAuthor("Default Author");
        return fallbackBook;
    }
}
