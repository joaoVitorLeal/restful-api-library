package io.github.joaoVitorLeal.libraryapi.validator;

import io.github.joaoVitorLeal.libraryapi.exceptions.BusinessRuleException;
import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Enforces two business rules:
 * 1. ISBN must be unique
 * 2. Books published after 2020 require price
 */
@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final int AND_PRICE_REQUIREMENT = 2020;
    private final BookRepository repository;

    public void validate(Book book) {
        if (doesBookExistsWithIsbn(book)) {
            throw new DuplicateRegistrationException("ISBN already registered.");
        }

        if (isMissingRequiredPrice(book)) {
            throw new BusinessRuleException("price", "For books with a publication year from 2020 onwards, the price is mandatory.");
        }
    }

    private boolean isMissingRequiredPrice(Book book) {
        return book.getPrice() == null && book.getPublicationDate().getYear() >= AND_PRICE_REQUIREMENT;
    }

    // Special ISBN check that permits updates to existing books
    private boolean doesBookExistsWithIsbn(Book book) {
        Optional<Book> possibleBook = repository.findByIsbn(book.getIsbn());

        if(book.getId() == null) {
            return possibleBook.isPresent();
        }

        return possibleBook.map(Book::getId).stream().anyMatch(id -> !id.equals(book.getId()));
    }
}
