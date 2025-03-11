package io.github.joaoVitorLeal.libraryapi.services;

import io.github.joaoVitorLeal.libraryapi.models.Client;
import io.github.joaoVitorLeal.libraryapi.repositories.ClientRepository;
import io.github.joaoVitorLeal.libraryapi.validator.ClientValidator;
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

    public Optional<Client> getByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }
}
