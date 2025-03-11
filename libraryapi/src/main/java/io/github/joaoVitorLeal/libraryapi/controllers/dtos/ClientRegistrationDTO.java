package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

import static io.github.joaoVitorLeal.libraryapi.constants.ValidationMessages.REQUIRED_FIELD_MESSAGE;

public record ClientRegistrationDTO(
        @NotBlank(message =  REQUIRED_FIELD_MESSAGE)
        String clientId,

        @NotBlank(message =  REQUIRED_FIELD_MESSAGE)
        String clientSecret,

        @NotBlank(message =  REQUIRED_FIELD_MESSAGE)
        String redirectURI,

        @NotBlank(message =  REQUIRED_FIELD_MESSAGE)
        String scope
) {
}
