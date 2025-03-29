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
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Authors") // Swagger documentation
@Slf4j
public class AuthorController implements GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Save", description = "Register a new author.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author successfully registered."),
            @ApiResponse(responseCode = "422", description = "Invalid input data."),
            @ApiResponse(responseCode = "409", description = "Author already exists.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorRegistrationDTO dto) {
        log.info("Registering new author: '{}'.", dto.name());

        Author author = mapper.toEntity(dto);
        service.save(author);
        URI location = headerLocationGenerator(author.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'OPERATOR')")
    @Operation(summary = "Get an Author By ID", description = "Retrieve author details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found."),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable("id") String id) {
        var authorId = UUID.fromString(id);

        return service
                .getById(authorId)
                .map(author -> {
                    AuthorResponseDTO dto = mapper.toDTO(author);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'OPERATOR')")
    @Operation(summary = "Search", description = "Search authors by name or nationality.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors retrieved successfully.")
    })
    public ResponseEntity<List<AuthorResponseDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality) {

        List<Author> authorList = service.searchAuthorsByExample(name, nationality);
        List<AuthorResponseDTO> dtoList = authorList
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update", description = "Update an existing author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author updated successfully."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "409", description = "Author conflicts with existing data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody @Valid AuthorRegistrationDTO dto) {
        log.info("Updating author ID: '{}'.", id);

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
    @Operation(summary = "Delete", description = "Delete an existing author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "400", description = "Author cannot be deleted (e.g., linked books exist).")
    })
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting author ID: '{}'.", id);
        var authorId = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(authorId);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(authorOptional.get());
        return ResponseEntity.noContent().build();
    }
}
