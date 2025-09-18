package org.example.library.repository;

import org.example.library.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAvailable(boolean available);
    List<Book> findByAuthorId(UUID authorId);
    // вариант 1: через EntityGraph
    @EntityGraph(attributePaths = "author")
    List<Book> findAllBy(); // не перекрывает дефолтный findAll()
}
