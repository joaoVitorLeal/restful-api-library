package io.github.joaoVitorLeal.libraryapi.services;

import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder; // Criptografia configurada. @see SecurityConfiguration

    public void save(User user) {
        var password = user.getPassword();
        user.setPassword(encoder.encode(password));

        repository.save(user);
    }

    public void update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário que o usuário já esteja cadastrado na base de dados.");
        }
        repository.save(user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<User> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(User user) {
        repository.delete(user);
    }
}
