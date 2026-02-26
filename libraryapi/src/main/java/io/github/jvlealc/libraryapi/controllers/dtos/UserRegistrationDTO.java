package io.github.jvlealc.libraryapi.controllers.dtos;


import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.github.jvlealc.libraryapi.constants.ValidationMessages.*;

public record UserRegistrationDTO(
        UUID id,

        @NotBlank(message =  REQUIRED_FIELD_MESSAGE)
        @Size(min = 2, max = 100, message = FIELD_SIZE_MESSAGE)
        String name,

        @Past(message = INVALID_DATE_MESSAGE)
        LocalDate birthDate,

        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        @Size(min = 2, max = 20, message = FIELD_SIZE_MESSAGE)
        String username,

        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        @Size(min = 6, message = MIN_PASSWORD_LENGTH_MESSAGE)
        String password,

        @Email(message = INVALID_EMAIL_MESSAGE)
        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        String email,

        @NotEmpty(message = REQUIRED_FIELD_MESSAGE)
        List<String> roles
    ) {
}
