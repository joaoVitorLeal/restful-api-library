package io.github.jvlealc.libraryapi.repositories;

import io.github.jvlealc.libraryapi.models.Author;
import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.models.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    List<Book> findByAuthor(Author author);

    List<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleAndPrice(String title, BigDecimal price);

    List<Book> findByTitleOrIsbn(String title, String isbn);

    List<Book> findByPublicationDateBetween(LocalDate init, LocalDate end);

    @Query("select b from Book as b order by b.title, b.price")
    List<Book> listAllByTitleAndPrice();

    @Query("select a from Book b join b.author a")
    List<Author> listAuthorsByBooks();

    @Query("select distinct b.title from Book b")
    List<String> listDistinctBookTitles();

    // Special case: Brazilian authors genre filter with custom JPQL join
    @Query("SELECT b.genre FROM Book b JOIN b.author a WHERE a.nationality = 'Brazilian'")
    List<String> listGenreOfBrazilianAuthors();

    @Query("""
            SELECT b FROM Book b WHERE b.genre = :genre ORDER BY :orderParam
            """)
    List<Book> findBygenre(@Param("genre") BookGenre bookGenre, @Param("orderParam") String orderProperty);

    @Query( "SELECT b FROM Book b WHERE b.genre = ?1 ORDER BY ?2" )
    List<Book> findByGenrePositionalParameters(BookGenre bookGenre, String propertyName);

    @Modifying
    @Transactional
    @Query("delete from Book where genre = :genre")
    void deleteByGenre(@Param("genre") BookGenre genre);

    @Modifying
    @Transactional
    @Query("update Book set publicationDate = :newDate where id = :bookId ")
    void updatePublicationDate(@Param("newDate") LocalDate newPublicationDate, @Param("bookId") UUID id);

    boolean existsByAuthor(Author author);
}