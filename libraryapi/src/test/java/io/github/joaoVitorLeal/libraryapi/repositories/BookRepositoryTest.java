package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class BookRepositoryTest { // Classes de teste não necessitam ser 'public'

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void saveTest() { // Da mesma forma que as classes de teste, os seus métodos não necessitam ter modificadores de visibilidade (public, private, etc)
        Book book = new Book();
        book.setIsbn("10001-50405");
        book.setValue(BigDecimal.valueOf(300.25));
        book.setGenre(BookGenre.SCIENCE);
        book.setTitle("Amphibious");
        book.setPublicationDate(LocalDate.of(2012 , 5, 11));

        Author maria = authorRepository.findById(UUID.fromString("f1365ac3-4ca7-4d01-ae16-8bc735948bae")).orElse(null);
        book.setAuthor(maria);

        bookRepository.save(book);
    }


    @Test
    void saveAuthorAndBookTest() {
        // Criando autor
        Author author = new Author();
        author.setName("Carlos");
        author.setBirthDate(LocalDate.of(1988, 2, 9));
        author.setNationality("Brazilian");

        // Criando livro
        Book book = new Book();
        book.setIsbn("302003-000023");
        book.setValue(BigDecimal.valueOf(125.39));
        book.setGenre(BookGenre.SCIENCE);
        book.setTitle("Matemática para Burros");
        book.setPublicationDate(LocalDate.of(2014, 10, 28));

        // Salvando o autor no repositório
        authorRepository.save(author);

        // Definindo o autor do livro
        book.setAuthor(author);

        // Salvando o livro no repositório
        bookRepository.save(book);
    }


    /**
     * Métod0 salvar utilizando recurso Cascade (efeito cascata).
     * Cascade:
     *  O atributo "cascade" permite que todas as operações realizadas na entidade "Book" (como persistir, atualizar ou remover)
     * sejam automaticamente aplicadas à entidade "Author" associada. Isso cria um efeito "em cascata", propagando as ações
     * de uma entidade para outra relacionada.
     * MENOS UTILIZADO; RISCO DE DELEÇÃO ACIDENTAL;
     * */
    @Test
    void saveCascadeTest() {
       // Criando livro
        Book book = new Book();
        book.setIsbn("555332-000010");
        book.setValue(BigDecimal.valueOf(125.39));
        book.setGenre(BookGenre.MYSTERY);
        book.setTitle("Detective in Action");
        book.setPublicationDate(LocalDate.of(1980 , 3, 1));

        // Criando autor
        Author author = new Author();
        author.setName("Juan");
        author.setBirthDate(LocalDate.of(1959, 12, 29));
        author.setNationality("Bolivian");

        // Definindo o autor do livro
        book.setAuthor(author);
        // Salvando o livro e, com o recurso cascade o autor //
        bookRepository.save(book); // Em background será executado a criação do autor seguido da criação do livro
    }



    @Test
    void listTest() {
        List<Book> bookList = bookRepository.findAll();
        bookList.forEach(System.out::println);
    }

    @Test
    void updateAuthorOfBookTest() {
        UUID id = UUID.fromString("c2cfd5e9-e2b4-4d2e-821b-cf054e12cffc"); // 'id' of Detective in Action (DELETADO) - Juan
        var bookForUpdate = bookRepository.findById(id).orElse(null);

        // Obtendo novo autor
        UUID idAuthor = UUID.fromString("e35a2261-c589-432c-b16a-1243762cd427"); // id do 'José'
        Author jose = authorRepository.findById(idAuthor).orElse(null); // Novo autor

        // Setando o novo autor do livro
        bookForUpdate.setAuthor(jose);

        // Atualizando livro
        bookRepository.save(bookForUpdate);
    }

    @Test
    void deleteTest () { // Deletando por objeto "book"
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
     * Deleção em Cascata!
     * Tanto o livro como o autor vinculado a ele, foram deletados.
     * */
    @Test
    void deleteCascadeTest() {
        UUID id = UUID.fromString("4f7038c4-dd87-43eb-8c2f-dd8b7c279833");
        bookRepository.deleteById(id);
    }

    @Test
    @Transactional // Utilizado com a finalidade de carregamento de dados de outras entidade relacionadas ao livro.
                  // Isto é necessário devido ao comportamento do fetch = FetchType.LAZY utilizado na declaração da relação na classe Book
    void searchBookTest() {
        UUID id = UUID.fromString("9c113714-7122-4a1d-9aab-980d72dccd6d");
        Book book = bookRepository.findById(id).orElse(null);

        System.out.println("Livro:");
        System.out.println(book.getTitle());

        System.out.println("Autor:");
        System.out.println(book.getAuthor().getName());
    }

    @Test
    void searchByTitleTest() {
        List<Book> bookList = bookRepository.findByTitle("El Robo de La Casa Encantada");
        bookList.forEach(System.out::println);
    }

    @Test
    void searchByISBNTest() {
        List<Book> bookList = bookRepository.findByIsbn("20012-22999");
        bookList.forEach(System.out::println);
    }

    @Test
    void searchByTitleAndValueTest() {
        String searchTitle = "UFO";
        BigDecimal searchValue = BigDecimal.valueOf(100);

        List<Book> bookList = bookRepository.findByTitleAndValue(searchTitle, searchValue);
        bookList.forEach(System.out::println);
    }


    /**
     * Exemplos JPQL e @Query:
     */

    @Test
    void searchByTitleOrISBNTest() {
        // Neste exemplo vamos utilizar o título do livro para pesquisa
        String searchTitle = "Corazon";

        List<Book> bookList = bookRepository.findByTitleOrIsbn(searchTitle, null);
        bookList.forEach(System.out::println);
    }

    @Test
    void bookListWithQueryJPQL() {
        var result = bookRepository.listAllByTitleAndValue();
        result.forEach(System.out::println);
    }

    @Test
    void listAuthorsOfBooks() { // Listar autores dos livros
        var result = bookRepository.listAuthorsByBooks();
        result.forEach(System.out::println);
    }

    @Test
    void listUnrepeatedBookTitles() { // Listar títulos não repetidos. OBS: existem dois livros chamados 'UFO' no database
        var result = bookRepository.listDistinctBookTitles();
        result.forEach(System.out::println);
    }

    @Test
    void listGenreBooksOfBrazilianAuthors() {
        var result = bookRepository.listGenreOfBrazilianAuthors();
        result.forEach(System.out::println);
    }

    // Named Parameters
    @Test
    void listByGenreQueryNamedParam() {
        var result = bookRepository.findBygenre(BookGenre.FICTION, "publicationDate");
        result.forEach(System.out::println);
    }

    // Positional Parameters
    @Test
    void listByGenreQueryPositionalParam() {
        var result = bookRepository.findByGenrePositionalParameters(BookGenre.FICTION, "value");
        result.forEach(System.out::println);
    }


    ///  Realizando operações de escrita(UPDATE, DELETE) com Queries

    @Test
    void deleteByGenreTest() { // Deletando livro por gênero
        bookRepository.deleteByGenre(BookGenre.SCIENCE);
    }

    @Test
    void updatePublicationDateById() { // Atualizando data de publicação do livro
        var id = UUID.fromString("612c39a7-b93e-44fa-8584-65d8acd07ef9");
        var newPublicationDate = LocalDate.of(2001, 1, 1);

        bookRepository.updatePublicationDate(newPublicationDate, id);
    }
}
