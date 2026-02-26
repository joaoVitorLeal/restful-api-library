package io.github.jvlealc.libraryapi.controllers.dtos;

public record ValidationErrorDTO(
        String field,
        String error
    ) {
}
