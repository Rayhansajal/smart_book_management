package com.example.smart_book_management.controller;

import com.example.smart_book_management.dto.BookDTO;
import com.example.smart_book_management.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    // Home Page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // List Books + Search
    @GetMapping
    public String listBooks(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("books", bookService.searchBooks(keyword));
            model.addAttribute("keyword", keyword.trim());
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        return "books/list";
    }

    // Show Add Book Form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/add";
    }

    // Save New Book
    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") BookDTO bookDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "books/add";
        }

        try {
            bookService.saveBook(bookDTO);
            redirectAttributes.addFlashAttribute("success", " Book added successfully!");
            return "redirect:/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books/add";
        }
    }

    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("book", bookService.getBookById(id));
            return "books/edit";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }

    // Update Book
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") BookDTO bookDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("book", bookDTO);
            return "books/edit";
        }

        try {
            bookService.updateBook(id, bookDTO);
            redirectAttributes.addFlashAttribute("success", " Book updated successfully!");
            return "redirect:/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }

    // Delete Book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", " Book deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
    }

    // Book Detail
    @GetMapping("/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("book", bookService.getBookById(id));
            return "books/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }
}