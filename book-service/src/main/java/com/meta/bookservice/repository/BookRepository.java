package com.meta.bookservice.repository;

import com.meta.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface BookRepository extends JpaRepository<Book, Long> {
}