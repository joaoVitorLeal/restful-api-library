package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.UserRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.mappers.UserMapper;
import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements GenericController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid UserRegistrationDTO dto) {
        User user = mapper.toEntity(dto);
        service.save(user);
        URI uri = headerLocationGenerator(user.getId());
        return ResponseEntity.created(uri).build();
    }
}
