package io.github.joaoVitorLeal.libraryapi.controllers;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Provides a default implementation for generating location headers in RESTful responses.
 * Controllers implementing this interface can use {@link #headerLocationGenerator(UUID)}
 * to build URIs for newly created resources.
 */
public interface GenericController {

    default URI headerLocationGenerator (UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
