package io.github.joaoVitorLeal.libraryapi.security;

import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.security.utils.PasswordGenerator;
import io.github.joaoVitorLeal.libraryapi.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Handles successful OAuth2 social logins by:
 * 1. Converting OAuth2 user data to the internal User model.
 * 2. Auto-registering new users with default privileges.
 * 3. Replacing OAuth2 authentication with custom authentication.
 */
@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String DEFAULT_PASSWORD = PasswordGenerator.generateRandomPassword();

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        User user = userService.findByEmail(email);

        if(user == null) {
            user = registerUserInDataBase(email, name);
        }

        // Replace OAuth2 authentication with custom authentication.
        authentication = new CustomAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    // New users via social login are assigned the 'OPERATOR' role by default.
    private @NotNull User registerUserInDataBase(String email, String name) {
        User user;
        user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(getUsernameByEmail(email));
        user.setPassword(DEFAULT_PASSWORD);
        user.setRoles(List.of("OPERATOR"));

        userService.save(user);
        return user;
    }

    private String getUsernameByEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}