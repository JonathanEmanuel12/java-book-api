package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(
        "SELECT b FROM Book b WHERE " +
       "LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
       "LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
       "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :search, '%'))"
    )
    Page<Book> findBySearch(@Param("search") String search, Pageable pageable);
}
