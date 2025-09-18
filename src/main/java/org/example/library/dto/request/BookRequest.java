package org.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


import java.util.UUID;

public record BookRequest(@NotBlank(message = "Title is required")
                          String title,
                          @NotBlank
                          @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN must be 10 or 13 digits")
                          String isbn,
                          @NotNull(message = "Author ID is required")
                          UUID authorId) {
}
