package com.example.smart_book_management.repository;

import com.example.smart_book_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
            SELECT b FROM Book b 
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY 
                CASE 
                    WHEN LOWER(b.title) LIKE LOWER(CONCAT(:keyword, '%')) THEN 1
                    WHEN LOWER(b.author) LIKE LOWER(CONCAT(:keyword, '%')) THEN 2
                    ELSE 3
                END
            """)
    List<Book> searchBooks(@Param("keyword") String keyword);
}
