package io.github.jvlealc.libraryapi.controllers.mappers;

import io.github.jvlealc.libraryapi.controllers.dtos.AuthorRegistrationDTO;
import io.github.jvlealc.libraryapi.controllers.dtos.AuthorResponseDTO;
import io.github.jvlealc.libraryapi.models.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorRegistrationDTO dto);

    AuthorResponseDTO toDTO(Author author);
}
