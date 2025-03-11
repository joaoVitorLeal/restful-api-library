package io.github.joaoVitorLeal.libraryapi.controllers.mappers;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.UserRegistrationDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.UserResponseDTO;
import io.github.joaoVitorLeal.libraryapi.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationDTO dto);

    UserResponseDTO toDTO(User user);
}
