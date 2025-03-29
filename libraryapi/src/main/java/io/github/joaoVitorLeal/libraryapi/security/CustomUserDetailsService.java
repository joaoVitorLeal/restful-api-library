package io.github.joaoVitorLeal.libraryapi.security;

import io.github.joaoVitorLeal.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User; // importação do Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Adapts the internal User model to Spring Security's UserDetails.
 */
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        io.github.joaoVitorLeal.libraryapi.models.User userModel = service.findByUsername(username);

        if (userModel == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .roles(userModel.getRoles().toArray(new String[userModel.getRoles().size()]))
                .build();
    }
}
