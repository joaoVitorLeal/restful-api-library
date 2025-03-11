package io.github.joaoVitorLeal.libraryapi.controllers.mappers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ClientRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ClientResponseDTO;
import io.github.joaoVitorLeal.libraryapi.models.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRegistrationDTO dto);

    ClientResponseDTO toDTO(Client client);
}
