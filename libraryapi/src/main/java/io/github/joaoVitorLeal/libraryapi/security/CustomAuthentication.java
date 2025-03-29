package io.github.joaoVitorLeal.libraryapi.security;

import io.github.joaoVitorLeal.libraryapi.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 Representing personalized user authentication with their permissions (roles) and other details.
 This class is used to PROVIDE information about the authenticated user in the system.
 */
@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final User user;

    // Converts our role strings into Spring Security GrantedAuthority objects
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.user
                .getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override public Object getCredentials() {
        return null;
    }
    @Override public Object getDetails() {
        return user;
    }
    @Override public Object getPrincipal() {
        return user;
    }
    @Override public boolean isAuthenticated() {
        return true;
    }
    @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }
    @Override public String getName() {
        return user.getUsername();
    }
}
