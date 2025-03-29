package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByClientId(String clientId);
}
