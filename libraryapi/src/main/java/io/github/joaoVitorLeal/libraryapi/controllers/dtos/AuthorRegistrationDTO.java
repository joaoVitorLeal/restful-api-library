package io.github.joaoVitorLeal.libraryapi.controllers.dtos;

import io.github.joaoVitorLeal.libraryapi.models.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorRegistrationDTO(
        UUID id,

        @NotBlank(message = "Campo obrigatório.")
        @Size(min = 2, max = 100, message = "campo fora do tamanho padrão.")
        String name,

        @NotNull(message = "Campo obrigatório.")
        @Past(message = "Data inválida.")
        LocalDate birthDate,

        @NotBlank(message = "Campo obrigatório.")
        @Size(min = 2, max = 100, message = "campo fora do tamanho padrão.")
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
