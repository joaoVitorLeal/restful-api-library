package io.github.joaoVitorLeal.libraryapi.services;

import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.repositories.UserRepository;
import io.github.joaoVitorLeal.libraryapi.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserValidator validator;

    public void save(User user) {
        var password = user.getPassword();
        user.setPassword(encoder.encode(password));
        validator.validate(user);
        repository.save(user);
    }

    public void update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("To update, the user must already be registered in the database.");
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
