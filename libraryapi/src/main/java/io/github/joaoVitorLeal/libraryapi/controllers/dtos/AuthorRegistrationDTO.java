package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

import static io.github.joaoVitorLeal.libraryapi.constants.ValidationMessages.*;

public record AuthorRegistrationDTO(
        UUID id,

        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        @Size(min = 2, max = 100, message = FIELD_SIZE_MESSAGE)
        String name,

        @NotNull(message = REQUIRED_FIELD_MESSAGE)
        @Past(message = INVALID_DATE_MESSAGE)
        LocalDate birthDate,

        @NotBlank(message = REQUIRED_FIELD_MESSAGE)
        @Size(min = 2, max = 100, message = FIELD_SIZE_MESSAGE)
        String nationality
) {

//    public Author mappingForAuthor() { // Mapeia os dados de authorDTO para entidade Author e retorna um objeto do tipo Author
//        Author author = new Author();
//        author.setName(this.name);
//        author.setBirthDate(this.birthDate);
//        author.setNationality(this.nationality);
//        return author;
//    }
}
