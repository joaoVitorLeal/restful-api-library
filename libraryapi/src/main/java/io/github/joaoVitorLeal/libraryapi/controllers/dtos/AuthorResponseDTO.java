package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID id,
        String name,
        LocalDate birthDate,
        String nationality
    ) {
}
