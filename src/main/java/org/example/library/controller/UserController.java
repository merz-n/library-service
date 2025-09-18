package org.example.library.controller;

import jakarta.validation.Valid;
import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.dto.response.UserResponse;
import org.example.library.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public List<UserResponse> getAll() { return service.getAllUsers(); }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) { return service.getUserById(id); }

    @GetMapping("/by-email")
    public UserResponse getByEmail(@RequestParam String email) { return service.getUserByEmail(email); }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse create(@RequestBody @Valid UserRequest request) {
        return service.createUser(request);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @RequestBody @Valid UserRequest request) {
        return service.updateUser(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteUser(id);
    }

    // факультатив: список выданных книг конкретному пользователю
    @GetMapping("/{id}/books")
    public List<BookResponse> borrowed(@PathVariable UUID id) {
        return service.getBorrowedBooks(id);
    }
}