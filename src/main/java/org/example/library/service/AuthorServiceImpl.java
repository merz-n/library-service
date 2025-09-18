package org.example.library.service;

import org.example.library.dto.request.AuthorRequest;
import org.example.library.dto.response.AuthorResponse;
import org.example.library.exception.ConflictException;
import org.example.library.exception.NotFoundException;
import org.example.library.mapper.AuthorMapper;
import org.example.library.model.Author;
import org.example.library.repository.AuthorRepository;
import org.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             BookRepository bookRepository,
                             AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll()
                .stream().map(authorMapper::toResponse).toList();
    }

    @Override
    public AuthorResponse getAuthorById(UUID id) {
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));
        return authorMapper.toResponse(a);
    }

    @Override
    public List<AuthorResponse> getAuthorByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name)
                .stream().map(authorMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public AuthorResponse createAuthor(AuthorRequest request) {
        Author a = authorMapper.toEntity(request);
        a = authorRepository.save(a);
        return authorMapper.toResponse(a);
    }

    @Override
    @Transactional
    public AuthorResponse updateAuthor(UUID id, AuthorRequest request) {
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));
        authorMapper.updateEntity(request, a);
        return authorMapper.toResponse(a);
    }

    @Override
    @Transactional
    public void deleteAuthor(UUID id) {
        if (!authorRepository.existsById(id)) {
            throw new NotFoundException("Author not found: " + id);
        }
        // запретим удалять, если привязаны книги
        if (!bookRepository.findByAuthorId(id).isEmpty()) {
            throw new ConflictException("Author has books and cannot be deleted");
        }
        authorRepository.deleteById(id);
    }
}
