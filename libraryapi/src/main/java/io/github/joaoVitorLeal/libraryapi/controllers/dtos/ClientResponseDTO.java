package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import java.util.UUID;

public record ClientResponseDTO(
        UUID id,
        String clientId,
        String clientSecret,
        String redirectURI,
        String scope
    ) {
}
