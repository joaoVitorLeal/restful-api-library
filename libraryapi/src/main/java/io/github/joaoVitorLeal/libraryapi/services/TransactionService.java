package io.github.joaoVitorLeal.libraryapi.services;

import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import io.github.joaoVitorLeal.libraryapi.repositories.AuthorRepository;
import io.github.joaoVitorLeal.libraryapi.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * TEST-ONLY CLASS - TRANSACTION DEMONSTRATION PURPOSES.
 * NOT MEANT FOR PRODUCTION USE.
 *
 * Demonstrates:
 * 1. JPA transactional behavior with rollback
 * 2. Dirty checking mechanism
 * 3. Manual API testing scenarios
 */

@Service
public class TransactionService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void updateWithoutUpdating() {
        var book = bookRepository
                .findById(UUID.fromString("aa528823-8827-4bd6-a243-8e293d095bc5"))
                .orElse(null);

        book.setPublicationDate(LocalDate.of(2024, 6, 9));
    }

    @Transactional
    public void execute() {
        Author author = new Author();
        author.setName("Teste Carlos");
        author.setBirthDate(LocalDate.of(1988, 2, 9));
        author.setNationality("Brazilian");
        authorRepository.save(author);

        Book book = new Book();
        book.setIsbn("302003-000023");
        book.setPrice(BigDecimal.valueOf(125.39));
        book.setGenre(BookGenre.SCIENCE);
        book.setTitle("Teste Matemática para Burros");
        book.setPublicationDate(LocalDate.of(2024, 10, 28));
        book.setAuthor(author);
        bookRepository.save(book);

        if (author.getName().equals("Teste Carlos")) {
            throw new RuntimeException("ROLLBACK");
        }
    }
}
