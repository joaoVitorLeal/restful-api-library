package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.BookRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ErrorResponse;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.BookMapper;
import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BookRegistrationDTO dto) {
        try {
            Book book = mapper.toEntity(dto); // MAPEAR DTO PARA ENTIDADE - UTILIZANDO O mapstruct

            service.save(book);  // ENVIAR A ENTIDADE PARA O SERVICE VALIDAR E SALVAR NO DATABASE
            // CRIAR URL PARA ACESSO DOS DADOS DO LIVRO
            // RETORNAR CODIGO CREATED COM HEADER LOCATION


            return ResponseEntity.ok(book);

        } catch (DuplicateRegistrationException e) {
            var errorDto = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDto.status()).body(errorDto);
        }

    }
}
