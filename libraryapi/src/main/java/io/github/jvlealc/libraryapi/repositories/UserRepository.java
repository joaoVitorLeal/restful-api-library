package io.github.jvlealc.libraryapi.repositories;

import io.github.jvlealc.libraryapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String email);

    // Used for business rule validation
    Optional<User> findByUsernameOrEmail(String username, String email);
}
