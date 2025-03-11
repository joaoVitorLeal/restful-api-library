package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorResponseDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.AuthorMapper;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.security.SecurityService;
import io.github.joaoVitorLeal.libraryapi.services.AuthorService;
import io.github.joaoVitorLeal.libraryapi.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors") // http://localhost:8080/authors
@RequiredArgsConstructor // Injeção automática dos atributos que sejam FINAL no construtor
public class AuthorController implements GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorRegistrationDTO dto) { // @RequestBody - Refere-se ao objeto vindo no corpo da requisição. Sendo transformado em um DTO
        Author author = mapper.toEntity(dto);
        service.save(author);
        URI location = headerLocationGenerator(author.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'OPERATOR')")
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
    public ResponseEntity<Void> update(
            @PathVariable("id") String id,
            @RequestBody @Valid AuthorRegistrationDTO authorRegistrationDto) {

        var authorId = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(authorId);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var author = authorOptional.get();
        author.setName(authorRegistrationDto.name());
        author.setBirthDate(authorRegistrationDto.birthDate());
        author.setNationality(authorRegistrationDto.nationality());

        service.update(author);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        var authorId = UUID.fromString(id);
        Optional<Author> authorOptional = service.getById(authorId);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(authorOptional.get());
        return ResponseEntity.noContent().build();
    }
}
