package io.github.jvlealc.libraryapi.controllers;

import io.github.jvlealc.libraryapi.controllers.dtos.BookRegistrationDTO;
import io.github.jvlealc.libraryapi.controllers.dtos.BookSearchResultDTO;
import io.github.jvlealc.libraryapi.controllers.mappers.BookMapper;
import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.models.BookGenre;
import io.github.jvlealc.libraryapi.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Books") // Swagger documentation
public class BookController implements GenericController {

    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Save", description = "Register a new book.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book successfully registered."),
            @ApiResponse(responseCode = "422", description = "Invalid input data."),
            @ApiResponse(responseCode = "409", description = "Book already exists.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid BookRegistrationDTO dto) {
        Book book = mapper.toEntity(dto);
        service.save(book);
        var url = headerLocationGenerator(book.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Get a book by ID", description = "Retrieve book details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    public ResponseEntity<BookSearchResultDTO> getBook(@PathVariable String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                  var dto = mapper.toDTO(book);
                  return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Delete", description = "Remove a book from the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    service.delete(book);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Search", description = "Retrieve a paginated list of books based on filters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found."),
            @ApiResponse(responseCode = "403", description = "Access denied.")
    })
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
    @Operation(summary = "Update", description = "Modify book details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "422", description = "Validation error.")
    })
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
