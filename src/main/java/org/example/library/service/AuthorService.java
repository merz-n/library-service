package org.example.library.service;

import org.example.library.dto.request.AuthorRequest;
import org.example.library.dto.response.AuthorResponse;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    List<AuthorResponse> getAllAuthors();
    AuthorResponse getAuthorById(UUID id);
    List<AuthorResponse> getAuthorByName(String name);
    AuthorResponse createAuthor(AuthorRequest request);
    AuthorResponse updateAuthor(UUID id, AuthorRequest request);
    void deleteAuthor(UUID id);
}