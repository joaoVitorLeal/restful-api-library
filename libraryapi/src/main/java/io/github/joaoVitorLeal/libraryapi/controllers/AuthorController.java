package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorResponseDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.AuthorMapper;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors") // http://localhost:8080/authors
@RequiredArgsConstructor // Injeção automática dos atributos que sejam FINAL no construtor
@Tag(name = "Authors") // Doc: Customização da documentação (Swagger)
@Slf4j // Habilita um objeto de log para classe. Logs inseridos em save(), update(), delete()
public class AuthorController implements GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Save", description = "Register a new author.") // Doc
    @ApiResponses({ // Doc: Possíveis respostas à esta requisição. Recebe um Array de ApiResponse
            @ApiResponse(responseCode = "201", description = "Successfully registered."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Author already exists.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorRegistrationDTO dto) { // @RequestBody - Refere-se ao objeto vindo no corpo da requisição. Sendo transformado em um DTO
       log.info("Register new author of name '{}'.", dto.name()); // Log: de cadastro de autor

        Author author = mapper.toEntity(dto);
        service.save(author);
        URI location = headerLocationGenerator(author.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'OPERATOR')")
    @Operation(summary = "Get an Author By ID", description = "Retrieve an author's data by ID.") // Doc
    @ApiResponses({ // Doc
            @ApiResponse(responseCode = "200", description = "Author found."),
            @ApiResponse(responseCode = "422", description = "Author not found.")
    })
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable("id") String id) {
        var authorId = UUID.fromString(id);

        return service
                .getById(authorId)
                .map(author -> {

                    AuthorResponseDTO dto = mapper.toDTO(author);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());  // .build() é usado para finalizar a construção de uma ResponseEntity sem corpo.
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'OPERATOR')")
    @Operation(summary = "Search", description = "Search for authors using name and nationality parameters.") // Doc
    @ApiResponses({ // Doc
            @ApiResponse(responseCode = "200", description = "Author found."),
    })
    public ResponseEntity<List<AuthorResponseDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality) {

        List<Author> authorList = service.searchAuthorsByExample(name, nationality);
        List<AuthorResponseDTO> dtoList = authorList
                .stream()
                // Para cada elemento author, o métod0 toDTO da instância mapper é chamado.
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update", description = "Update an existing author") // Doc
    @ApiResponses({ // Doc
            @ApiResponse(responseCode = "204", description = "Successfully updated."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "409", description = "Author already exists."),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody @Valid AuthorRegistrationDTO dto) {

        log.info("Updating the author of id '{}' and name '{}'.", id, dto.name()); // Log: de atualização de autor

        var authorId = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(authorId);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var author = authorOptional.get();
        author.setName(dto.name());
        author.setBirthDate(dto.birthDate());
        author.setNationality(dto.nationality());

        service.update(author);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete", description = "Delete an existing author") // Doc
    @ApiResponses({ // Doc
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "400", description = "Author has registered books and cannot be deleted.")
    })
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting the author of ID: {}.", id); // Log: de deleção de autor
        var authorId = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(authorId);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(authorOptional.get());
        return ResponseEntity.noContent().build();
    }
}
