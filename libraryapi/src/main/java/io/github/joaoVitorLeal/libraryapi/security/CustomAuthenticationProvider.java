package io.github.joaoVitorLeal.libraryapi.security;

import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom authentication provider that validates user credentials
 * and assigns roles during the authentication process.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String enteredPassword = authentication.getCredentials().toString();

        User foundUserByUsername = userService.findByUsername(username);

        if (foundUserByUsername == null) {
            throw getUsernameNotFoundException();
        }

        String encryptedPassword = foundUserByUsername.getPassword();
        boolean doPasswordsMatch = encoder.matches(enteredPassword, encryptedPassword);

        if(doPasswordsMatch) {
            return new CustomAuthentication(foundUserByUsername);
        }

        throw getUsernameNotFoundException();
    }

    // Creates a UsernameNotFoundException with a custom message
    private static UsernameNotFoundException getUsernameNotFoundException() {
        return new UsernameNotFoundException("Incorrect username and/or password!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
