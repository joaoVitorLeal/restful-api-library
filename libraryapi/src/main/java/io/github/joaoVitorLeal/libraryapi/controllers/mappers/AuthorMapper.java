package io.github.joaoVitorLeal.libraryapi.controllers.mappers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorResponseDTO;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorRegistrationDTO dto);

    AuthorResponseDTO toDTO(Author author);
}
