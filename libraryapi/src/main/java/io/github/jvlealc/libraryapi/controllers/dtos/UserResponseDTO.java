package io.github.jvlealc.libraryapi.controllers.dtos;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        List<String> roles
) {
}
