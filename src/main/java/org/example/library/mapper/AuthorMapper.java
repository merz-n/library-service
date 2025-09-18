package org.example.library.mapper;

import org.example.library.dto.request.AuthorRequest;
import org.example.library.dto.response.AuthorResponse;
import org.example.library.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toEntity(AuthorRequest request) {
        Author a = new Author();
        a.setName(request.name());
        a.setBirthDate(request.birthDate());
        return a;
    }

    public void updateEntity(AuthorRequest request, Author target) {
        target.setName(request.name());
        target.setBirthDate(request.birthDate());
    }

    public AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getBirthDate()
        );
    }
}
