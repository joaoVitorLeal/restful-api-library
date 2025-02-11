package io.github.joaoVitorLeal.libraryapi.controllers.mappers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.BookRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.BookSearchResultDTO;
import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.repositories.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    // Mapeia de DTO para Entidade
    @Mapping(target = "author", expression = "java( authorRepository.findById(dto.authorId()).orElse(null) )")
    public abstract Book toEntity(BookRegistrationDTO dto);

    // Mapeia de Entidade para DTO
    public abstract BookSearchResultDTO toDTO(Book book);
}
