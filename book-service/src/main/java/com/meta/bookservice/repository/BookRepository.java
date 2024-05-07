package com.meta.bookservice.repository;

import com.meta.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}