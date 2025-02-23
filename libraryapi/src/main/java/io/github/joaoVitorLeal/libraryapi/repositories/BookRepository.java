package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
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

/**
 * @see BookRepositoryTest
 */

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    /**
     * Para Maiores detalhes sobre os Queries Method's:
     * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     *
     * Query Method: Recupera uma lista de livros associados a um determinado autor.
     * Este método pode retornar uma lista vazia, um único objeto ou múltiplos objetos,
     * dependendo dos resultados encontrados.
     * Internamente, o JPA executa a seguinte consulta SQL:
     * SELECT * FROM book WHERE id_author = ?;
     *
     * @param author O autor dos livros a serem recuperados.
     * @return Lista de livros associados ao autor fornecido.
     */
    List<Book> findByAuthor(Author author);


    /**
     * Query Method: Recupera uma lista de livros cujo título corresponde ao parâmetro fornecido.
     * A busca é feita através do campo 'title' da tabela 'book'.
     *
     * @param title O título do livro a ser pesquisado.
     * @return Lista de livros que possuem o título especificado.
     */
    List<Book> findByTitle(String title);


    /**
     * SELECT * FROM book WHERE isbn = ?;
     *
     * @param isbn O ISBN do livro a ser pesquisado.
     * @return livro que possui o ISBN especificado.
     */
    Optional<Book> findByIsbn(String isbn);


    /**
     * Query Method: Recupera uma lista de livros que correspondem simultaneamente ao título e ao valor fornecidos.
     * Utiliza o operador lógico AND para combinar as condições de busca.
     * Internamente, o JPA executa a seguinte consulta SQL:
     * SELECT * FROM book WHERE title = ? AND value = ?;
     *
     * @param title O título do livro a ser pesquisado.
     * @param value O valor associado ao livro.
     * @return Lista de livros que correspondem aos critérios de busca.
     */
    List<Book> findByTitleAndValue(String title, BigDecimal value);


    /**
     * Query Method: Recupera uma lista de livros que correspondem a qualquer um dos critérios de busca fornecidos:
     * título ou ISBN. Utiliza o operador lógico OR para combinar as condições de busca.
     * Internamente, o JPA executa a seguinte consulta SQL:
     * SELECT * FROM book WHERE title = ? OR isbn = ?;
     *
     * @param title O título do livro a ser pesquisado.
     * @param isbn  O ISBN do livro a ser pesquisado.
     * @return Lista de livros que correspondem a qualquer um dos critérios de busca.
     */
    List<Book> findByTitleOrIsbn(String title, String isbn);


    /**
     * Query Method - Retorna uma lista de livros cuja data de publicação está entre as datas de início e fim fornecidas.
     * A consulta SQL executada é equivalente a:
     * SELECT * FROM book WHERE publication_date BETWEEN ? AND ?;
     *
     * @param init Data de início do intervalo de publicação.
     * @param end  Data de fim do intervalo de publicação.
     * @return Lista de livros publicados dentro do intervalo especificado.
     */
    List<Book> findByPublicationDateBetween (LocalDate init, LocalDate end);

    //=============================================================================//

    /**
     * Trabalhando com consultas JPQL e @Query
     * JPQL -> referencia as entidades (Classes) e as propriedades */
    // Retornando uma lista de livros ordenada pelo título e preço
    // SQL -> select b.* from book as b order by b.title
    @Query("select b from Book as b order by b.title, b.value")
    List<Book> listAllByTitleAndValue();

    /**
     * SQL ->
     * select a.*
     * from book b
     * join author a on a.id = b.id_author
     * @return author's
     */
    // Retorna uma lista de autores por livro
    @Query("select a from Book b join b.author a")
    List<Author> listAuthorsByBooks();

    // Retorna títulos distintos dos livros cadastrados no database
    @Query("select distinct b.title from Book b")
    List<String> listDistinctBookTitles();

    /**
     * SQL ->
     * select distinct b.genre
     * from book b
     * join author a on a.id = b.id_author
     * where a.nationality = 'Brazilian'
     * order by b.genre
     * @return: genre
     */
    @Query("""
            SELECT
                b.genre
            FROM
                Book b
            JOIN
                b.author a
            WHERE
                a.nationality = 'Brazilian'
            ORDER BY
                b.genre
            """)
    List<String> listGenreOfBrazilianAuthors();


    /// Utilizando Queries JPQL com Named Parameters -> parâmetros nomeados ///
    @Query("""
            SELECT b FROM Book b WHERE b.genre = :genre ORDER BY :paramDeOrdenacao
            """)    // Substituímos os :placeholders pelos parâmetros do métod0 utilizando a @Annotation -> @Param("nome do parâmetro") seguido do tipo (Author, String, etc) e nome da variável
    List<Book> findBygenre(@Param("genre") BookGenre bookGenre, @Param("paramDeOrdenacao") String nomePropriedade);


    /// Utilizando Queries JPQL com Positional Parameters -> parâmetros posicionais ///
    @Query( "SELECT b FROM Book b WHERE b.genre = ?1 ORDER BY ?2" )
    List<Book> findByGenrePositionalParameters(BookGenre bookGenre, String nomePropriedade);


    /**
     * Métodos para realização de operações de UPDATE e DELETE.
     */

    @Modifying // @Annotation necessária para realizar operação de escrita, pois iremos modificar um registro
    @Transactional // @Annotation necessária para realizar operação de escrita, pois precisaremos abrir uma transação para operações de escrita(UPDATE, INSERT, DELETE)
    @Query("delete from Book where genre = :genre")
    void deleteByGenre(@Param("genre") BookGenre genre);

    @Modifying
    @Transactional
    @Query("update Book set publicationDate = :newDate where id = :bookId ")
    void updatePublicationDate(@Param("newDate") LocalDate newPublicationDate, @Param("bookId") UUID id);


    boolean existsByAuthor(Author author);

    /// Utilizando a extensão do JpaSpecification para operações no db



}
