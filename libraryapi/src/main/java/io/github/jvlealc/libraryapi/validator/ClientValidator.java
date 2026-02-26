package io.github.jvlealc.libraryapi.validator;

import io.github.jvlealc.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.jvlealc.libraryapi.models.Client;
import io.github.jvlealc.libraryapi.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Ensures clientId remains unique across all registrations
 */
@Component
@RequiredArgsConstructor
public class ClientValidator {

    private final ClientRepository repository;

    public void validate(Client client) {
        if (isClientRegistered(client)) {
            throw new DuplicateRegistrationException("Duplicate Registration");
        }
    }

    // Handles edge case where client updates shouldn't fail on their own clientId
    private boolean isClientRegistered(Client client) {
        Optional<Client> foundClient = repository.findByClientId(client.getClientId());

        if (client.getId() == null) {
            return foundClient.isPresent();
        }

        return !client.getId().equals(foundClient.get().getId()) && foundClient.isPresent();
    }
}
