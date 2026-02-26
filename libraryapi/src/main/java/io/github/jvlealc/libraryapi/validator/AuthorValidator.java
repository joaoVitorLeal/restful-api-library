package io.github.jvlealc.libraryapi.validator;

import io.github.jvlealc.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.jvlealc.libraryapi.models.Author;
import io.github.jvlealc.libraryapi.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Prevents duplicate author registrations by checking unique combination of:
 * name + birthdate + nationality
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorValidator {

    private final AuthorRepository repository;

    public void validate(Author author) {
        log.info("Validating author: {}", author);
        if (isAuthorRegistered(author)){
            log.warn("Duplicate registration attempt: {}", author);
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

        if (author.getId() == null) {
            return possibleAuthor.isPresent();
        }

        return possibleAuthor
                .filter(value -> !value.getId().equals(author.getId()))
                .isPresent();
    }
}
