package org.example.library.service;

import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.dto.response.UserResponse;
import org.example.library.exception.ConflictException;
import org.example.library.exception.NotFoundException;
import org.example.library.mapper.BookMapper;
import org.example.library.mapper.UserMapper;
import org.example.library.model.User;
import org.example.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           BookMapper bookMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return userMapper.toResponse(u);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email: " + email));
        return userMapper.toResponse(u);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(x -> {
            throw new ConflictException("Email already in use: " + request.email());
        });
        User u = userMapper.toEntity(request);
        u = userRepository.save(u);
        return userMapper.toResponse(u);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserRequest request) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        if (!u.getEmail().equalsIgnoreCase(request.email())
                && userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("Email already in use: " + request.email());
        }
        userMapper.updateEntity(request, u);
        return userMapper.toResponse(u);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<BookResponse> getBorrowedBooks(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        return u.getBorrowedBooks().stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    public boolean hasBorrowedBooks(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        return !u.getBorrowedBooks().isEmpty();
    }
}
