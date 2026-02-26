package io.github.jvlealc.libraryapi.repositories;

import io.github.jvlealc.libraryapi.models.Author;
import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.models.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepositor;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void saveTest() {
        Author author = new Author();
        author.setName("John");
        author.setBirthDate(LocalDate.of(1990, 7, 4));
        author.setNationality("American");

        var savedAuthor = authorRepositor.save(author);
        System.out.println("Saved author: " + savedAuthor);
    }

    @Test
    public void updateTest() {
        var id = UUID.fromString("e8023bad-a3d5-4775-aadf-3c9fc6a93020");
        Optional<Author> possibleAuthor = authorRepositor.findById(id);

        if (possibleAuthor.isPresent()) {
            Author foundAuthor = possibleAuthor.get();
            foundAuthor.setName("Tânia");
            foundAuthor.setBirthDate(LocalDate.of(1964, 12, 27));
            foundAuthor.setNationality("Russian");
            authorRepositor.save(foundAuthor);
        }
    }

    /**
     * Demonstrates secure password generation
     */
    @Test
    public void generateSecurePassword() {
        SecureRandom random= new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String securePassword = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Base64 encoded password: " + securePassword);
    }

    @Test
    public void listTest() {
        List<Author> authorsList = authorRepositor.findAll();
        authorsList.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Author count: " + authorRepositor.count());
    }

    @Test
    void searchAuthorTest() {
        UUID id = UUID.fromString("8cef4f0e-28f5-45cd-8db4-caca17a91a36");
        Optional<Author> author = authorRepositor.findById(id);
        author.ifPresent(System.out::println);
    }

    @Test
    public void deleteByIdTest() {
        var id = UUID.fromString("...");
        Optional<Author> possibleAuthor = authorRepositor.findById(id);
        possibleAuthor.ifPresent(author -> authorRepositor.deleteById(id));
    }

    @Test
    public void deleteTest() {
        var id = UUID.fromString("...");
        Optional<Author> possibleAuthor = authorRepositor.findById(id);
        possibleAuthor.ifPresent(authorRepositor::delete);
    }

    /**
     * Tests author-book relationship persistence
     */
    @Test
    void saveAuthorWithBookListTest() {
        Author author = new Author();
        author.setName("Antônio");
        author.setNationality("Spanish");
        author.setBirthDate(LocalDate.of(2000, 8, 10));

        Book book = new Book();
        book.setIsbn("20012-22999");
        book.setPrice(BigDecimal.valueOf(357.58));
        book.setGenre(BookGenre.MYSTERY);
        book.setTitle("El Robo de La Casa Encantada");
        book.setPublicationDate(LocalDate.of(2020 , 3, 1));
        book.setAuthor(author);

        Book book2 = new Book();
        book2.setIsbn("20012-22999");
        book2.setPrice(BigDecimal.valueOf(85));
        book2.setGenre(BookGenre.ROMANCE);
        book2.setTitle("Corazon");
        book2.setPublicationDate(LocalDate.of(2016 , 1, 15));
        book2.setAuthor(author);

        author.setBooks(new ArrayList<>());
        author.getBooks().add(book);
        author.getBooks().add(book2);

        authorRepositor.save(author);
        bookRepository.saveAll(author.getBooks());
    }

    /**
     * Tests custom query method for lazy loading workaround
     */
    @Test
    void listAuthorBooksTest() {
        var id = UUID.fromString("8cef4f0e-28f5-45cd-8db4-caca17a91a36");
        var author = authorRepositor.findById(id).get();
        List<Book> bookList = bookRepository.findByAuthor(author);
        author.setBooks(bookList);
        author.getBooks().forEach(System.out::println);
    }
}
