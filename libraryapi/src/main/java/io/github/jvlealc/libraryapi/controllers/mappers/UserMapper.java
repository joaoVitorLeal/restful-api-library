package io.github.jvlealc.libraryapi.controllers.mappers;

import io.github.jvlealc.libraryapi.controllers.dtos.UserRegistrationDTO;
import io.github.jvlealc.libraryapi.controllers.dtos.UserResponseDTO;
import io.github.jvlealc.libraryapi.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationDTO dto);

    UserResponseDTO toDTO(User user);
}
