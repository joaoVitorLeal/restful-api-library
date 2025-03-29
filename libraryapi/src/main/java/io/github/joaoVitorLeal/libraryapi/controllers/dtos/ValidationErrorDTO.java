package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

public record ValidationErrorDTO(
        String field,
        String error
    ) {
}
