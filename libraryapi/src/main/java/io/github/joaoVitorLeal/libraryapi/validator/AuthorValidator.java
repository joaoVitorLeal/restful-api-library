package io.github.joaoVitorLeal.libraryapi.validator;

import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor // Injeção automática dos atributos que sejam FINAL no construtor
public class AuthorValidator {

    private final AuthorRepository repository;

    public void validate(Author author) {
        if (isAuthorRegistered(author)){
            throw new DuplicateRegistrationException("Duplicate Registration");
        }
    }

    private boolean isAuthorRegistered(Author author) {
        Optional<Author> possibleAuthor = repository.findByNameAndBirthDateAndNationality(
                author.getName(),
                author.getBirthDate(),
                author.getNationality()
        );

        if (author.getId() == null){
            return possibleAuthor.isPresent();
        }

        //    Se o autor TEM ID (É UMA ATUALIZAÇÃO DE CADASTRO EXISTENTE):
        //    - Retorna true APENAS se:
        //      a) Existe um autor com os mesmos dados (possibleAuthor.isPresent() = true).
        //      b) O ID do autor encontrado é DIFERENTE do ID do autor que está sendo atualizado.
        //    - Isso evita conflito ao atualizar o PRÓPRIO autor.
        // Retorno:
        //    true → Outro autor já usa esses dados (duplicidade ❌).
        //    false → Dados são válidos (ou é o próprio autor sendo atualizado ✅).
        return !author.getId().equals(possibleAuthor.get().getId()) && possibleAuthor.isPresent();
    }
}
