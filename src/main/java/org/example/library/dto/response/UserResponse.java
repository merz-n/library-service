package org.example.library.dto.response;

import java.util.UUID;

public record UserResponse(UUID id,
                           String name,
                           String email) {
}