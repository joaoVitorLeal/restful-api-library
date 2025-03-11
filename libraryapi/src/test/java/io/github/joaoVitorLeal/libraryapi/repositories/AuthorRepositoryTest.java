package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
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

        Optional<Author> possibleAuthor =  authorRepositor.findById(id);

        if (possibleAuthor.isPresent()) {

            Author foundAuthor = possibleAuthor.get();
            System.out.println("Author's data:");
            System.out.println(foundAuthor);

            foundAuthor.setName("Tânia");
            foundAuthor.setBirthDate(LocalDate.of(1964, 12, 27));
            foundAuthor.setNationality("Russian");

            authorRepositor.save(foundAuthor);

        }
    }

    @Test
    public void generateSecurePassword() {
        SecureRandom random= new SecureRandom();
        byte[] bytes = new byte[16]; // 16 bytes = 128 bits
        random.nextBytes(bytes);

        String securePassword = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Senha codificada em Base64: " + securePassword);

        byte[] decoderBytes = Base64.getDecoder().decode(securePassword);
        System.out.println("O bytes decodificados: " +  Arrays.toString(decoderBytes));
    }

    @Test
    public void listTest() {
        List<Author> authorsList = authorRepositor.findAll();
        authorsList.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Contagem de autores: " + authorRepositor.count());
    }

    @Test
    void searchAuthorTest() {
        UUID id = UUID.fromString("8cef4f0e-28f5-45cd-8db4-caca17a91a36");
        Optional<Author> author = authorRepositor.findById(id);

        if (author.isPresent()) {
            System.out.println(author.get());
        }
    }

    @Test
    public void deleteByIdTest() {
        var id = UUID.fromString("...");
        Optional<Author> possibleAuthor = authorRepositor.findById(id);

        if (possibleAuthor.isPresent()) {
            Author foundAuthor = possibleAuthor.get();
            authorRepositor.deleteById(id);
        }
    }

    @Test
    public void deleteTest() {
        var id = UUID.fromString("...");
        Optional<Author> possibleAuthor = authorRepositor.findById(id);

        if (possibleAuthor.isPresent()) {
            Author foundAuthor = possibleAuthor.get();
            authorRepositor.delete(foundAuthor);
        }
    }

    @Test
    void saveAuthorWithBookListTest() {
        Author author = new Author();
        author.setName("Antônio");
        author.setNationality("Spanish");
        author.setBirthDate(LocalDate.of(2000, 8, 10));

        // Instanciando livro
        Book book = new Book();
        book.setIsbn("20012-22999");
        book.setPrice(BigDecimal.valueOf(357.58));
        book.setGenre(BookGenre.MYSTERY);
        book.setTitle("El Robo de La Casa Encantada");
        book.setPublicationDate(LocalDate.of(2020 , 3, 1));
        book.setAuthor(author);
        // Instanciando o segundo livro
        Book book2 = new Book();
        book2.setIsbn("20012-22999");
        book2.setPrice(BigDecimal.valueOf(85));
        book2.setGenre(BookGenre.ROMANCE);
        book2.setTitle("Corazon");
        book2.setPublicationDate(LocalDate.of(2016 , 1, 15));
        book2.setAuthor(author);

        // Adicionando os livros instanciados à lista de livros do autor
        author.setBooks(new ArrayList<>());
        author.getBooks().add(book);
        author.getBooks().add(book2);

        // Primeiramente devamos salvar o autor no database e em seguida a lista de livros
        authorRepositor.save(author);

        // Salvando a lista de livros vinculado ao autor
        bookRepository.saveAll(author.getBooks());
    }

    /**
     * Este Métod0 realiza a busca de lista livros por id do autor informado.
     * Foi necessário criar uma busca de livros personalizada por meio de Query_Methods, criada em BookRepository,
     * devido ao comportamento LAZY da relação entre as tabelas.
     * */
    @Test
//    @Transactional // Garante que o métod0 seja executado dentro de um contexto de transação aberta, permitindo o gerenciamento automático de transações pelo Spring.
    void listAuthorBooksTest() {
        var id = UUID.fromString("8cef4f0e-28f5-45cd-8db4-caca17a91a36");
        var author = authorRepositor.findById(id).get();

        // Buscar os livros do autor
        List<Book> bookList = bookRepository.findByAuthor(author); // findByAuthor -> Query Methods (busca personalizada).
        author.setBooks(bookList);

        author.getBooks().forEach(System.out::println);



    }

}
