package io.github.joaoVitorLeal.libraryapi.validator;

import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookValidator {
    private final BookRepository repository;

    public void validate(Book book) {
        if (doesBookExistsWithIsbn(book)) {
            throw new DuplicateRegistrationException("ISBN já cadastrado.");
        }
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
