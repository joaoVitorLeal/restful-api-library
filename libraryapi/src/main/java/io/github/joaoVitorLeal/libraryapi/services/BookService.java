package io.github.joaoVitorLeal.libraryapi.services;

import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import io.github.joaoVitorLeal.libraryapi.repositories.BookRepository;
import io.github.joaoVitorLeal.libraryapi.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.joaoVitorLeal.libraryapi.repositories.specs.BookSpecs.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookValidator validator;

    public Book save(Book book) {
        validator.validate(book);
        return repository.save(book);
    }

    public Optional<Book> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    // isbn, titulo, nome autor, genero, ano de publicacao
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

        if (isbn != null) {
            // query = query and isbn = :isbn
            specs = specs.and(isbnEqual(isbn));
        }

        if (title != null) {
            specs = specs.and(titleLike(title));
        }

        if (genre != null) {
            specs = specs.and(genreEqual(genre));
        }

        if (authorName != null) {
            specs = specs.and(authorNameLike(authorName));
        }

        if (publicationYear != null) {
            specs = specs.and(publicationYearEqual(publicationYear));
        }

        Pageable pageRequest = PageRequest.of(page, pageSize); // PageRequest implementa a interface Pageable

        return repository.findAll(specs, pageRequest);
    }

    public void update(Book book) {
        if (book.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessário que o livro já esteja cadastrado na base de dados.");
        }

        validator.validate(book);
        repository.save(book);
    }
}
