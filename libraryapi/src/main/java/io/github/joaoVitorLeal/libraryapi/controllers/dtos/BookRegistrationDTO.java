package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import io.github.joaoVitorLeal.libraryapi.constants.ValidationMessages;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static io.github.joaoVitorLeal.libraryapi.constants.ValidationMessages.*;

public record BookRegistrationDTO(
        @ISBN(type = ISBN.Type.ANY, message = INVALID_ISBN_MESSAGE)
        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        String isbn,

        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        String title,

        @Past(message = INVALID_DATE_MESSAGE)
        @NotNull(message = REQUIRED_FIELD_MESSAGE)
        LocalDate publicationDate,

        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        BookGenre genre,

        BigDecimal price,

        @NotNull(message = REQUIRED_FIELD_MESSAGE)
        UUID authorId
    ) {
}
