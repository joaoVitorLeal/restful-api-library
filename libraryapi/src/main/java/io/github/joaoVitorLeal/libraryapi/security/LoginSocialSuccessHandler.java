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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication; // Obtendo a authentication que vinda do Google
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String name = oAuth2User.getAttribute("name");
        String birthday = oAuth2User.getAttribute("birthday");
        String email = oAuth2User.getAttribute("email");

        User user = userService.findByEmail(email);

        if(user == null) {
            user = registerUserInDataBase(email, name);

        }

        authentication = new CustomAuthentication(user); // Modificando o objeto authentication do Google para o authentication customizada da aplicação

        SecurityContextHolder.getContext().setAuthentication(authentication); // Adicionando a authentication customizada para o contexto do Security

        super.onAuthenticationSuccess(request, response, authentication); // Finaliza o processo de autenticação e dar continuidade ao fluxo
    }

    private @NotNull User registerUserInDataBase(String email, String name) {
        User user;
        user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(getUsernameByEmail(email));
        user.setPassword(DEFAULT_PASSWORD);
        user.setRoles(List.of("OPERATOR")); // Todos os usuários que fizerem autenticação via email terá a ROLE com menos privilégios


        userService.save(user);
        return user;
    }

    private String getUsernameByEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
