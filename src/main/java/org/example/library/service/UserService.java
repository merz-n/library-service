package org.example.library.service;

import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // CRUD операции
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email); // специфичный метод
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(UUID id, UserRequest request); // факультативно
    void deleteUser(UUID id); // факультативно

    // БИЗНЕС-МЕТОДЫ (факультативно)
    List<BookResponse> getBorrowedBooks(UUID userId); // книги пользователя
    boolean hasBorrowedBooks(UUID userId); // проверка долгов
}
