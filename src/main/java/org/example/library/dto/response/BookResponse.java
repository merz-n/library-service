package org.example.library.dto.response;

import java.util.UUID;

public record BookResponse(UUID id,
                           String title,
                           String isbn,
                           boolean available,
                           UUID authorId,
                           String authorName,
                           UUID borrowedByUserId){

}
