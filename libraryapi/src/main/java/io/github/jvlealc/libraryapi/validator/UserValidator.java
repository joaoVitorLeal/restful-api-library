package io.github.jvlealc.libraryapi.validator;

import io.github.jvlealc.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.jvlealc.libraryapi.models.User;
import io.github.jvlealc.libraryapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Prevents duplicate user registrations by checking unique fields of:
 * username and email
 */
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validate (User user) {
        if(isUserRegistered(user)) {
            throw new DuplicateRegistrationException("Duplicate Registration");
        }
    }

    private boolean isUserRegistered(User user) {
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userOptional.isEmpty()) {
            return false;
        }
        if (user.getId() == null) {
            return userOptional.isPresent();
        }
        return !userOptional.get().getId().equals(user.getId()) && userOptional.isPresent();
    }
}
