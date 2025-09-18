package org.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record AuthorRequest(@NotBlank(message = "Name is required")
                            String name,
                            LocalDate birthDate) {
}
