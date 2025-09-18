package org.example.library.repository;

import org.example.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findByNameContainingIgnoreCase(String namePart);
}

