package io.github.jvlealc.libraryapi.services;

import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.models.BookGenre;
import io.github.jvlealc.libraryapi.repositories.BookRepository;
import io.github.jvlealc.libraryapi.security.SecurityService;
import io.github.jvlealc.libraryapi.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.jvlealc.libraryapi.repositories.specs.BookSpecs.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final BookValidator validator;
    private final SecurityService securityService;

    public Book save(Book book) {
        validator.validate(book);
        book.setUser(securityService.getAuthenticatedUser());
        return repository.save(book);
    }

    public Optional<Book> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public Page<Book> search(
            String isbn,
            String title,
            BookGenre genre,
            String authorName,
            Integer publicationYear,
            Integer page,
            Integer pageSize
    ) {
        Specification<Book> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null)
            specs = specs.and(isbnEqual(isbn));

        if (title != null)
            specs = specs.and(titleLike(title));

        if (genre != null)
            specs = specs.and(genreEqual(genre));

        if (authorName != null)
            specs = specs.and(authorNameLike(authorName));

        if (publicationYear != null)
            specs = specs.and(publicationYearEqual(publicationYear));

        return repository.findAll(specs, PageRequest.of(page, pageSize));
    }

    public void update(Book book) {
        if (book.getId() == null) {
            throw new IllegalArgumentException("To update, the book must already be registered in the database.");
        }
        validator.validate(book);
        repository.save(book);
    }
}
