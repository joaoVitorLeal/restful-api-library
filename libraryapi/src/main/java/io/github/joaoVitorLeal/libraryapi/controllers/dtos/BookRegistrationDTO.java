package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookRegistrationDTO(
        @ISBN
        @NotBlank(message = "Campo Obrigatório")
        String isbn,

        @NotBlank(message = "Campo obrigatório.")
        String title,

        @NotNull(message = "Campo obrigatório.")
        @Past(message = "Data inválida. Só é permitido datas passadas!")
        LocalDate publicationDate,

        BookGenre genre,

        BigDecimal value,

        @NotNull(message = "Campo obrigatório.")
        UUID authorId
    ) {
}
