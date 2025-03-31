package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ClientRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.ClientMapper;
import io.github.joaoVitorLeal.libraryapi.models.Client;
import io.github.joaoVitorLeal.libraryapi.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Client") // Swagger documentation
@Slf4j
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Save", description = "Register a new client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client successfully registered."),

    })
    public void save(@RequestBody @Valid ClientRegistrationDTO dto) {
        log.info("Registering new client: ID='{}', scope='{}'.", dto.clientId(), dto.scope());
        Client client = mapper.toEntity(dto);
        service.save(client);
    }
}
