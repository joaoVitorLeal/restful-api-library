package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String email);
}
