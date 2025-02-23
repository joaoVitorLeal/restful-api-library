package io.github.joaoVitorLeal.libraryapi.services;

import io.github.joaoVitorLeal.libraryapi.exceptions.OperationNotPermittedException;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.repositories.AuthorRepository;
import io.github.joaoVitorLeal.libraryapi.repositories.BookRepository;
import io.github.joaoVitorLeal.libraryapi.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Injeção automática dos atributos que sejam FINAL no construtor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    public Author save (Author author) {
        validator.validate(author);
        return authorRepository.save(author);
    }

    public void update(Author author) {
        if(author.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário que o autor já esteja cadastrado na base de dados.");
        }

        validator.validate(author);
        authorRepository.save(author);
    }

    public Optional<Author> getById(UUID id) {
        return authorRepository.findById(id);
    }

    public void delete(Author author) {
        if(hasBook(author)) {
            throw new OperationNotPermittedException("Erro na exclusão: Não é permitido excluir um autor que possui livros cadastrados (registro está sendo utilizado por outras entidades.)");
        }
        authorRepository.delete(author);
    }

    public List<Author> searchAuthors(String name, String nationality) {
        if (name != null && nationality != null) {
            return authorRepository.findByNameAndNationality(name, nationality);
        }

        if (name != null) {
            return authorRepository.findByName(name);
        }

        if (nationality != null) {
            return authorRepository.findByNationality(nationality);
        }

        return authorRepository.findAll();
    }

    ///  Utilizando Query By Example para pesquisas dinâmicas ///
    public List<Author> searchAuthorsByExample(String name, String nationality) {
        var author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "birthDate", "createdAt") // Ignora os atributos da entidade que não farão parte da pesquisa. (Em casos o onde o recebe o author (entidade/objeto) como parâmetro)
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // Buscar por uma String que contém o texto informado
        Example<Author> authorExample = Example.of(author, matcher);
        return  authorRepository.findAll(authorExample);
    }

    public boolean hasBook(Author author) {
        return bookRepository.existsByAuthor(author);
    }
}
