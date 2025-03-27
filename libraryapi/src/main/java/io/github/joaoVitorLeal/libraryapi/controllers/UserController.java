package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.UserRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.UserMapper;
import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
@Slf4j
public class UserController implements GenericController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED) // Utilizado caso o méto-do não retorne um ResponseEntity.algumStatus() . Ou seja quando o méto-do é 'void'.
    @Operation(summary = "Save", description = "Register a new User.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully registered.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid UserRegistrationDTO dto) {
        log.info("Registered a new User of username '{}', e-mail '{}' and roles '{}'.", dto.username(), dto.email(), dto.roles());

        User user = mapper.toEntity(dto);
        service.save(user);
        URI uri = headerLocationGenerator(user.getId());
        return ResponseEntity.created(uri).build();
    }
}
