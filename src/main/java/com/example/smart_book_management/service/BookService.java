package com.example.smart_book_management.service;

import com.example.smart_book_management.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO saveBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);
    List<BookDTO> searchBooks(String keyword);
}
