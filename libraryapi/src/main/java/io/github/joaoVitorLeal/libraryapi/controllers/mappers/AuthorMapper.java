package io.github.joaoVitorLeal.libraryapi.controllers.mappers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.AuthorResponseDTO;
import io.github.joaoVitorLeal.libraryapi.models.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Transformando em um @Component do Spring para podermos injetá-lo onde for necessário
public interface AuthorMapper {

    // Converte DTO para Entidade
    //
    // @Mapping() ->  Caso o AuthorDTO tenha um campo cujo o nome seja diferente da Entidade Author. Sendo necessário fazer o mapeamento desse atributo, em que 'source' refere-se ao DTO e 'target' a Entidade
    // Neste caso não é necessário, somente foi deixado para fins didáticos! //
    @Mapping(source = "name" , target = "name")
    @Mapping(source = "birthDate" , target = "birthDate")
    @Mapping(source = "nationality" , target = "nationality")
    Author toEntity(AuthorRegistrationDTO dto);

    // Converte Entidade para DTO
    AuthorResponseDTO toDTO(Author author);
}
