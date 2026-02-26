package io.github.jvlealc.libraryapi.services;

import io.github.jvlealc.libraryapi.exceptions.OperationNotPermittedException;
import io.github.jvlealc.libraryapi.models.Author;
import io.github.jvlealc.libraryapi.models.User;
import io.github.jvlealc.libraryapi.repositories.AuthorRepository;
import io.github.jvlealc.libraryapi.repositories.BookRepository;
import io.github.jvlealc.libraryapi.security.SecurityService;
import io.github.jvlealc.libraryapi.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;
    private final SecurityService securityService;

    public Author save(Author author) {
        validator.validate(author);
        User user = securityService.getAuthenticatedUser();
        author.setUser(user);
        return authorRepository.save(author);
    }

    public void update(Author author) {
        if (author.getId() == null) {
            throw new IllegalArgumentException("Cannot update an author that does not exist.");
        }
        validator.validate(author);
        authorRepository.save(author);
    }

    public Optional<Author> getById(UUID id) {
        return authorRepository.findById(id);
    }

    public void delete(Author author) {
        if (hasBook(author)) {
            throw new OperationNotPermittedException("Cannot delete an author with registered books.");
        }
        authorRepository.delete(author);
    }

    public List<Author> searchAuthors(String name, String nationality) {
        if (name == null && nationality == null) {
            return authorRepository.findAll();
        }
        if (name != null && nationality != null) {
            return authorRepository.findByNameAndNationality(name, nationality);
        }
        return (name != null) ? authorRepository.findByName(name) : authorRepository.findByNationality(nationality);
    }

    // ByExample query
    public List<Author> searchAuthorsByExample(String name, String nationality) {
        var author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "birthDate", "createdAt")
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Author> authorExample = Example.of(author, matcher);
        return authorRepository.findAll(authorExample);
    }

    public boolean hasBook(Author author) {
        return bookRepository.existsByAuthor(author);
    }
}
