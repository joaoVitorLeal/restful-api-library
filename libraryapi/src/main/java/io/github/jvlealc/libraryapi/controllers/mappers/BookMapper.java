package io.github.jvlealc.libraryapi.controllers.mappers;

import io.github.jvlealc.libraryapi.controllers.dtos.BookRegistrationDTO;
import io.github.jvlealc.libraryapi.controllers.dtos.BookSearchResultDTO;
import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.repositories.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    @Mapping(target = "author", expression = "java( authorRepository.findById(dto.authorId()).orElse(null) )")
    public abstract Book toEntity(BookRegistrationDTO dto);

    @Mapping(target = "author")
    public abstract BookSearchResultDTO toDTO(Book book);
}
