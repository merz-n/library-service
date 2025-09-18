package org.example.library.mapper;


import org.example.library.dto.request.BookRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.model.Book;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        // author проставляется в сервисе после загрузки по authorId
        return book;
    }

    public void updateEntity(BookRequest request, Book target) {
        target.setTitle(request.title());
        target.setIsbn(request.isbn());
        // смену автора делаем в сервисе (после findById(request.authorId()))
    }

    public BookResponse toResponse(Book book) {
        UUID authorId = book.getAuthor() != null ? book.getAuthor().getId() : null;
        String authorName = book.getAuthor() != null ? book.getAuthor().getName() : null;
        UUID borrowedByUserId = book.getBorrowedBy() != null ? book.getBorrowedBy().getId() : null;

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.isAvailable(),
                authorId,
                authorName,
                borrowedByUserId
        );
    }
}