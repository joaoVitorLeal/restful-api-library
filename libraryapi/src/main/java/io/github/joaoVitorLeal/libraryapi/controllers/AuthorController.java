package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorResponseDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ErrorResponse;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.AuthorMapper;
import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.exceptions.EntityInUseException;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import io.github.joaoVitorLeal.libraryapi.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors") // http://localhost:8080/authors
@RequiredArgsConstructor // Injeção automática dos atributos que sejam FINAL no construtor
public class AuthorController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorRegistrationDTO dto){ // @RequestBody - Refere-se ao objeto vindo no corpo da requisição. Sendo transformado em um DTO
        try {
            Author author = mapper.toEntity(dto);
            service.save(author);

            // Header: Location - URI do recurso criado
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(author.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        } catch (DuplicateRegistrationException e) {
            var errorDto = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDto.status()).body(errorDto);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable("id") String id){
        var authorId = UUID.fromString(id);

        return service
                .getById(authorId)
                .map(author -> {

                    AuthorResponseDTO dto = mapper.toDTO(author);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );  // .build() é usado para finalizar a construção de uma ResponseEntity sem corpo.
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        try {
            var authorId = UUID.fromString(id);
            Optional<Author> authorOptional = service.getById(authorId);

            if (authorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            service.delete(authorOptional.get());
            return ResponseEntity.noContent().build();

        } catch (EntityInUseException e) {
            var errorDto = ErrorResponse.standardResponse(e.getMessage());
            return ResponseEntity.status(errorDto.status()).body(errorDto);
        }
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality){
        List<Author> authorList = service.searchAuthorsByExample(name, nationality);

        List <AuthorResponseDTO> dtoList = authorList
                .stream()
                // Para cada elemento author, o métod0 toDTO da instância mapper é chamado.
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);

        //        List<AuthorResponseDTO> dtoList = result
        //                .stream() // Cria uma Stream<Author> a partir da lista "result"
        //                .map(author -> new AuthorResponseDTO( // Transforma cada Author em AuthorResponseDTO
        //                        author.getId(),
        //                        author.getName(),
        //                        author.getBirthDate(),
        //                        author.getNationality())
        //                ).collect(Collectors.toList()); // Coleta os DTOs em uma nova lista (List<AuthorResponseDTO>)
        //        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody @Valid AuthorRegistrationDTO authorRegistrationDto) {
        try {
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
        } catch (DuplicateRegistrationException e) {
            var errorDto = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDto.status()).body(errorDto);
        }
    }
}
