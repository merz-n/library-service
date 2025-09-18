package org.example.library.service;

import org.example.library.dto.request.BookRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.exception.ConflictException;
import org.example.library.exception.NotFoundException;
import org.example.library.mapper.BookMapper;
import org.example.library.model.Author;
import org.example.library.model.Book;
import org.example.library.model.User;
import org.example.library.repository.AuthorRepository;
import org.example.library.repository.BookRepository;
import org.example.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           UserRepository userRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookResponse> getAllBooks() {
        // findAllBy() подгружает автора через @EntityGraph
        return bookRepository.findAllBy()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    public BookResponse getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        // простая защита от дубликатов ISBN
        bookRepository.findByIsbn(request.isbn()).ifPresent(b -> {
            throw new ConflictException("ISBN already exists: " + request.isbn());
        });

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new NotFoundException("Author not found: " + request.authorId()));

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        book.setAvailable(true);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BookResponse updateBook(UUID id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        // 1) Проверка уникальности ISBN, если меняем
        if (!book.getIsbn().equals(request.isbn())
                && bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new ConflictException("ISBN already exists: " + request.isbn());
        }

        // 2) Смена автора, если пришёл другой authorId
        if (book.getAuthor() == null || !book.getAuthor().getId().equals(request.authorId())) {
            Author author = authorRepository.findById(request.authorId())
                    .orElseThrow(() -> new NotFoundException("Author not found: " + request.authorId()));
            book.setAuthor(author);
        }

        // 3) Обновляем простые поля
        book.setTitle(request.title());
        book.setIsbn(request.isbn());

        // Hibernate dirty checking сохранит изменения
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        if (!book.isAvailable()) {
            throw new ConflictException("Cannot delete a borrowed book");
        }
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public BookResponse borrowBook(UUID bookId, UUID userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        if (!book.isAvailable()) {
            throw new ConflictException("Book is already borrowed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        book.setBorrowedBy(user);
        book.setAvailable(false);
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional
    public BookResponse returnBook(UUID bookId, UUID userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        if (book.isAvailable()) {
            throw new ConflictException("Book is not borrowed");
        }
        if (book.getBorrowedBy() == null || !book.getBorrowedBy().getId().equals(userId)) {
            throw new ConflictException("This user didn't borrow the book");
        }

        book.setBorrowedBy(null);
        book.setAvailable(true);
        return bookMapper.toResponse(book);
    }

    @Override
    public List<BookResponse> getAvailableBooks() {
        return bookRepository.findByAvailable(true)
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }
}
