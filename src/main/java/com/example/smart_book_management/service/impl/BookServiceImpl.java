package com.example.smart_book_management.service.impl;

import com.example.smart_book_management.dto.BookDTO;
import com.example.smart_book_management.entity.Book;
import com.example.smart_book_management.repository.BookRepository;
import com.example.smart_book_management.service.BookService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        Book saved = bookRepository.save(book);
        return modelMapper.map(saved, BookDTO.class);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existing.setTitle(bookDTO.getTitle());
        existing.setAuthor(bookDTO.getAuthor());
        existing.setIsbn(bookDTO.getIsbn());
        existing.setGenre(bookDTO.getGenre());
        existing.setDescription(bookDTO.getDescription());


        if (bookDTO.getTotalQuantity() > 0) {
            existing.setTotalQuantity(bookDTO.getTotalQuantity());
        }

        if (bookDTO.getTotalQuantity() != existing.getTotalQuantity()) {
            int difference = bookDTO.getTotalQuantity() - existing.getTotalQuantity();
            existing.setAvailableQuantity(Math.max(0, existing.getAvailableQuantity() + difference));
        }

        Book updated = bookRepository.save(existing);

        return modelMapper.map(updated, BookDTO.class);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDTO> searchBooks(String keyword) {
        // Clean the keyword
        if (keyword == null || (keyword = keyword.trim()).isEmpty()) {
            return getAllBooks();
        }

        // Optional: Skip search if keyword is too short
        if (keyword.length() < 2) {
            return getAllBooks();
        }

        List<Book> books = bookRepository.searchBooks(keyword);

        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();
    }
}
