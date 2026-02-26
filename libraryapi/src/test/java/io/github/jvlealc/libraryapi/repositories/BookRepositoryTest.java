package io.github.jvlealc.libraryapi.repositories;

import io.github.jvlealc.libraryapi.models.Author;
import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.models.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    /**
     * Tests basic book creation with author association
     */
    @Test
    void saveTest() {
        Book book = new Book();
        book.setIsbn("10001-50405");
        book.setPrice(BigDecimal.valueOf(300.25));
        book.setGenre(BookGenre.SCIENCE);
        book.setTitle("Amphibious");
        book.setPublicationDate(LocalDate.of(2012 , 5, 11));

        Author maria = authorRepository.findById(UUID.fromString("f1365ac3-4ca7-4d01-ae16-8bc735948bae")).orElse(null);
        book.setAuthor(maria);

        bookRepository.save(book);
    }

    /**
     * Tests separate persistence of author and book
     */
    @Test
    void saveAuthorAndBookTest() {
        Author author = new Author();
        author.setName("Carlos");
        author.setBirthDate(LocalDate.of(1988, 2, 9));
        author.setNationality("Brazilian");

        Book book = new Book();
        book.setIsbn("302003-000023");
        book.setPrice(BigDecimal.valueOf(125.39));
        book.setGenre(BookGenre.SCIENCE);
        book.setTitle("Matemática para Burros");
        book.setPublicationDate(LocalDate.of(2014, 10, 28));

        authorRepository.save(author);
        book.setAuthor(author);
        bookRepository.save(book);
    }

    /**
     * Tests JPA cascade persist behavior
     */
    @Test
    void saveCascadeTest() {
        Book book = new Book();
        book.setIsbn("555332-000010");
        book.setPrice(BigDecimal.valueOf(125.39));
        book.setGenre(BookGenre.MYSTERY);
        book.setTitle("Detective in Action");
        book.setPublicationDate(LocalDate.of(1980 , 3, 1));

        Author author = new Author();
        author.setName("Juan");
        author.setBirthDate(LocalDate.of(1959, 12, 29));
        author.setNationality("Bolivian");

        book.setAuthor(author);
        bookRepository.save(book);
    }

    @Test
    void listTest() {
        List<Book> bookList = bookRepository.findAll();
        bookList.forEach(System.out::println);
    }

    /**
     * Tests author reassignment for a book
     */
    @Test
    void updateAuthorOfBookTest() {
        UUID id = UUID.fromString("c2cfd5e9-e2b4-4d2e-821b-cf054e12cffc");
        var bookForUpdate = bookRepository.findById(id).orElse(null);

        UUID idAuthor = UUID.fromString("e35a2261-c589-432c-b16a-1243762cd427");
        Author jose = authorRepository.findById(idAuthor).orElse(null);

        bookForUpdate.setAuthor(jose);
        bookRepository.save(bookForUpdate);
    }

    @Test
    void deleteTest() {
        var id = UUID.fromString("...");
        Book book = bookRepository.findById(id).get();
        bookRepository.delete(book);
    }

    @Test
    void deleteByIdTest() {
        var id = UUID.fromString("67cafd36-2da6-4fcc-8ab9-ea709a566a36");
        bookRepository.deleteById(id);
    }

    /**
     * Tests cascade delete behavior
     */
    @Test
    void deleteCascadeTest() {
        UUID id = UUID.fromString("4f7038c4-dd87-43eb-8c2f-dd8b7c279833");
        bookRepository.deleteById(id);
    }

    /**
     * Tests lazy loading workaround
     */
    @Test
    @Transactional
    void searchBookTest() {
        UUID id = UUID.fromString("9c113714-7122-4a1d-9aab-980d72dccd6d");
        Book book = bookRepository.findById(id).orElse(null);
        System.out.println(book.getAuthor().getName());
    }

    @Test
    void searchByTitleTest() {
        List<Book> bookList = bookRepository.findByTitle("El Robo de La Casa Encantada");
        bookList.forEach(System.out::println);
    }

    @Test
    void searchByISBNTest() {
        Optional<Book> book = bookRepository.findByIsbn("20012-22999");
        book.ifPresent(System.out::println);
    }

    @Test
    void searchByTitleAndPriceTest() {
        String searchTitle = "UFO";
        BigDecimal searchPrice = BigDecimal.valueOf(100);
        List<Book> bookList = bookRepository.findByTitleAndPrice(searchTitle, searchPrice);
        bookList.forEach(System.out::println);
    }

    /*  JPQL and @Query examples  */

    @Test
    void searchByTitleOrISBNTest() {
        String searchTitle = "Corazon";
        List<Book> bookList = bookRepository.findByTitleOrIsbn(searchTitle, null);
        bookList.forEach(System.out::println);
    }

    @Test
    void bookListWithQueryJPQL() {
        var result = bookRepository.listAllByTitleAndPrice();
        result.forEach(System.out::println);
    }

    @Test
    void listAuthorsOfBooks() {
        var result = bookRepository.listAuthorsByBooks();
        result.forEach(System.out::println);
    }

    @Test
    void listUnrepeatedBookTitles() {
        var result = bookRepository.listDistinctBookTitles();
        result.forEach(System.out::println);
    }

    @Test
    void listGenreBooksOfBrazilianAuthors() {
        var result = bookRepository.listGenreOfBrazilianAuthors();
        result.forEach(System.out::println);
    }

    @Test // Named Parameters
    void listByGenreQueryNamedParam() {
        var result = bookRepository.findBygenre(BookGenre.FICTION, "publicationDate");
        result.forEach(System.out::println);
    }

    @Test // Positional Parameters
    void listByGenreQueryPositionalParam() {
        var result = bookRepository.findByGenrePositionalParameters(BookGenre.FICTION, "price");
        result.forEach(System.out::println);
    }

    /*  Performing write operations (UPDATE, DELETE) with Queries  */

    @Test
    void deleteByGenreTest() {
        bookRepository.deleteByGenre(BookGenre.SCIENCE);
    }

    @Test
    void updatePublicationDateById() {
        var id = UUID.fromString("612c39a7-b93e-44fa-8584-65d8acd07ef9");
        var newPublicationDate = LocalDate.of(2001, 1, 1);
        bookRepository.updatePublicationDate(newPublicationDate, id);
    }
}
