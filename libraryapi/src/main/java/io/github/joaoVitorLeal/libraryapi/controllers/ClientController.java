package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ClientRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.ClientMapper;
import io.github.joaoVitorLeal.libraryapi.models.Client;
import io.github.joaoVitorLeal.libraryapi.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Utilizado caso o méto-do não retorne um ResponseEntity.algumStatus() . Ou seja quando o méto-do é 'void'.
    @PreAuthorize("hasRole('MANAGER')")
    public void save(@RequestBody @Valid ClientRegistrationDTO dto) {
        Client client = mapper.toEntity(dto);
        service.save(client);
    }
}
