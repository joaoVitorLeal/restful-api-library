package io.github.jvlealc.libraryapi.security;

import io.github.jvlealc.libraryapi.models.User;
import io.github.jvlealc.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Provides access to the authenticated user from the security context.
 */
@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof CustomAuthentication customAuth) {
            return customAuth.getUser();
        }

        return null;
    }
}
