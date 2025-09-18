package org.example.library.controller;

import jakarta.validation.Valid;
import org.example.library.dto.request.BookRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService service;
    public BookController(BookService service) { this.service = service; }

    @GetMapping
    public List<BookResponse> getAll() {
        return service.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable UUID id) {
        return service.getBookById(id);
    }

    @GetMapping("/available")
    public List<BookResponse> getAvailable() {
        return service.getAvailableBooks();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookResponse create(@RequestBody @Valid BookRequest request) {
        return service.createBook(request);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable UUID id, @RequestBody @Valid BookRequest request) {
        return service.updateBook(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteBook(id);
    }

    // факультатив: выдача/возврат
    @PostMapping("/{id}/borrow")
    public BookResponse borrow(@PathVariable UUID id, @RequestParam UUID userId) {
        return service.borrowBook(id, userId);
    }

    @PostMapping("/{id}/return")
    public BookResponse returnBook(@PathVariable UUID id, @RequestParam UUID userId) {
        return service.returnBook(id, userId);
    }
}