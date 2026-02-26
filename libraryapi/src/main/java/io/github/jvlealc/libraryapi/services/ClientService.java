package io.github.jvlealc.libraryapi.services;

import io.github.jvlealc.libraryapi.models.Client;
import io.github.jvlealc.libraryapi.repositories.ClientRepository;
import io.github.jvlealc.libraryapi.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientValidator validator;
    private final PasswordEncoder encoder;

    public Client save(Client client) {
        validator.validate(client);
        client.setClientSecret(encoder.encode(client.getClientSecret()));
        return repository.save(client);
    }

    public Optional<Client> findByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }
}
