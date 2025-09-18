package org.example.library.controller;

import jakarta.validation.Valid;
import org.example.library.dto.request.AuthorRequest;
import org.example.library.dto.response.AuthorResponse;
import org.example.library.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService service;
    public AuthorController(AuthorService service) { this.service = service; }

    @GetMapping
    public List<AuthorResponse> getAll() { return service.getAllAuthors(); }

    @GetMapping("/{id}")
    public AuthorResponse getById(@PathVariable UUID id) { return service.getAuthorById(id); }

    @GetMapping("/search")
    public List<AuthorResponse> search(@RequestParam String name) {
        return service.getAuthorByName(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AuthorResponse create(@RequestBody @Valid AuthorRequest request) {
        return service.createAuthor(request);
    }

    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable UUID id, @RequestBody @Valid AuthorRequest request) {
        return service.updateAuthor(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteAuthor(id);
    }
}