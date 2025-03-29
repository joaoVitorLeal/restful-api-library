package io.github.joaoVitorLeal.libraryapi.validator;

import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Prevents duplicate author registrations by checking unique combination of:
 * name + birthdate + nationality
 */
@Component
@RequiredArgsConstructor // Injeção automática dos atributos que sejam FINAL no construtor
public class AuthorValidator {

    private final AuthorRepository repository;

    public void validate(Author author) {
        if (isAuthorRegistered(author)){
            throw new DuplicateRegistrationException("Duplicate Registration");
        }
    }

    // Special case: Allows updates to existing author while blocking duplicates
    private boolean isAuthorRegistered(Author author) {
        Optional<Author> possibleAuthor = repository.findByNameAndBirthDateAndNationality(
                author.getName(),
                author.getBirthDate(),
                author.getNationality()
        );

        if (author.getId() == null){
            return possibleAuthor.isPresent();
        }

        return !author.getId().equals(possibleAuthor.get().getId()) && possibleAuthor.isPresent();
    }
}
