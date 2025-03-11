package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.BookRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.BookSearchResultDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.BookMapper;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import io.github.joaoVitorLeal.libraryapi.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements GenericController {

    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')") // Se esta @Annotation for utilizada em cima da classe, essa configuração já será aplicada em todos os endpoints.
    public ResponseEntity<Void> save(@RequestBody @Valid BookRegistrationDTO dto) {
        Book book = mapper.toEntity(dto); // MAPEAR DTO PARA ENTIDADE - UTILIZANDO O mapstruct
        service.save(book); // ENVIAR A ENTIDADE PARA O SERVICE VALIDAR E SALVAR NO DATABASE
        var url = headerLocationGenerator(book.getId()); // CRIAR URL PARA ACESSO DOS DADOS DO LIVRO
        return ResponseEntity.created(url).build(); // RETORNAR CÓDIGO CREATED COM HEADER LOCATION

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<BookSearchResultDTO> getBook(@PathVariable String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                  var dto = mapper.toDTO(book);
                  return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    service.delete(book);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Page<BookSearchResultDTO>> searchBooks(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "book-genre", required = false) BookGenre genre,
            @RequestParam(value = "author-name", required = false) String authorName,
            @RequestParam(value = "publication-year", required = false) Integer publicationYear,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "2") Integer pageSize
    ) {
        var searchResultPage = service.search(isbn, title, genre, authorName, publicationYear, page, pageSize);

        Page<BookSearchResultDTO> resultDtoPage = searchResultPage.map(mapper::toDTO);

        return ResponseEntity.ok(resultDtoPage);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody @Valid BookRegistrationDTO dto){
        return service.getById(UUID.fromString(id))
                .map(book -> {

                    Book mappedBook = mapper.toEntity(dto);

                    book.setIsbn(mappedBook.getIsbn());
                    book.setTitle(mappedBook.getTitle());
                    book.setPublicationDate(mappedBook.getPublicationDate());
                    book.setGenre(mappedBook.getGenre());
                    book.setPrice(mappedBook.getPrice());
                    book.setAuthor(mappedBook.getAuthor());

                    service.update(book);

                    return ResponseEntity.noContent().build();

                }).orElseGet( () -> ResponseEntity.notFound().build());
    }
}
