package org.example.library.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponse(UUID id,
                             String name,
                             LocalDate birthDate) {
}
