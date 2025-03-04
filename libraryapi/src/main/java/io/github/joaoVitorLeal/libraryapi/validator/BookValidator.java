package io.github.joaoVitorLeal.libraryapi.validator;

import io.github.joaoVitorLeal.libraryapi.exceptions.BusinessRuleException;
import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final int AND_PRICE_REQUIREMENT = 2020;
    private final BookRepository repository;

    public void validate(Book book) {
        if (doesBookExistsWithIsbn(book)) {
            throw new DuplicateRegistrationException("ISBN já cadastrado.");
        }

        if (isMissingRequiredPrice(book)) {
            throw new BusinessRuleException("value", "Para livros com ano de publicação a partir de 2020, o preço é obrigatório.");
        }
    }

    private boolean isMissingRequiredPrice(Book book) {
        return book.getValue() == null &&
                book.getPublicationDate().getYear() >= AND_PRICE_REQUIREMENT;
    }

    private boolean doesBookExistsWithIsbn(Book book) {
        Optional<Book> possibleBook = repository.findByIsbn(book.getIsbn());

        if(book.getId() == null) {
            return possibleBook.isPresent();
        }

        // Verifica se existe um livro com o mesmo ISBN, mas com um ID diferente do livro fornecido.
        // Se 'possibleBook' contém um livro, obtemos seu ID, transformamos em um Stream e verificamos
        // se algum ID é diferente do ID do livro passado como argumento.
        return possibleBook
                .map(Book::getId)
                .stream()
                .anyMatch(id -> !id.equals(book.getId()));
    }
}
