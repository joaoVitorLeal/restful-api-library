package io.github.jvlealc.libraryapi.controllers.dtos;

import io.github.jvlealc.libraryapi.models.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookSearchResultDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate publicationDate,
        BookGenre genre,
        BigDecimal price,
        AuthorResponseDTO author
    ) {
}
