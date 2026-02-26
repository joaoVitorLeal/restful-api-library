package io.github.jvlealc.libraryapi.controllers.mappers;

import io.github.jvlealc.libraryapi.controllers.dtos.ClientRegistrationDTO;
import io.github.jvlealc.libraryapi.controllers.dtos.ClientResponseDTO;
import io.github.jvlealc.libraryapi.models.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRegistrationDTO dto);

    ClientResponseDTO toDTO(Client client);
}
