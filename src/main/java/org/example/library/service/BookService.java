package org.example.library.service;

import org.example.library.dto.request.BookRequest;
import org.example.library.dto.response.BookResponse;

import java.util.List;
import java.util.UUID;

public interface BookService {
    // CRUD операции
    List<BookResponse> getAllBooks();
    BookResponse getBookById(UUID id);
    BookResponse createBook(BookRequest request);
    BookResponse updateBook(UUID id, BookRequest request);
    void deleteBook(UUID id);

    // БИЗНЕС-МЕТОДЫ (не CRUD!)

    BookResponse borrowBook(UUID bookId, UUID userId);  // выдать книгу
    BookResponse returnBook(UUID bookId, UUID userId);  // вернуть книгу
    List<BookResponse> getAvailableBooks();             // доступные книги
}
