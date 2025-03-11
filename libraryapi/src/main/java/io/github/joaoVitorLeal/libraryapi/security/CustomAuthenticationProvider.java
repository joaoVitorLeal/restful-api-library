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
 * Provedor de autenticação customizado para a aplicação.
 * Este provedor realiza a verificação das credenciais do usuário,
 * a validação da senha e a atribuição de roles para o processo de autenticação.
 */
@Component // Registra o CustomAuthenticationProvider como o provedor de autenticação da aplicação
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    /**
     * Realiza a autenticação personalizada.
     * Este méto-do recebe as credenciais do usuário (nome de usuário e senha),
     * busca o usuário correspondente no banco de dados e valida a senha.
     *
     * @param authentication Objeto que contém as credenciais do usuário (nome e senha)
     * @return Authentication Token com as credenciais verificadas
     * @throws AuthenticationException Se o usuário não for encontrado ou a senha for incorreta
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String enteredPassword = authentication.getCredentials().toString();

        // Busca o usuário no banco de dados pelo nome de usuário
        User foundUserByUsername = userService.findByUsername(username);

        if (foundUserByUsername == null) {
            throw getUsernameNotFoundException();
        }

        String encryptedPassword = foundUserByUsername.getPassword();
        boolean doPasswordsMatch = encoder.matches(enteredPassword, encryptedPassword); // Verifica se a senha digitada corresponde à senha armazenada

        if(doPasswordsMatch) {
            return new CustomAuthentication(foundUserByUsername); // Retorna a autenticação com as informações do usuário
        }

        throw getUsernameNotFoundException();
    }

    private static UsernameNotFoundException getUsernameNotFoundException() {
        return new UsernameNotFoundException("Usuário e/ou senha incorretos!");
    }

    /**
     * Informa quais tipos de authentications que esse provide suporta.
     *
     * @param authentication A classe de autenticação a ser verificada
     * @return true se o provedor suportar o tipo de autenticação, caso contrário false
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // Tipo de autenticação suportada: UsernamePasswordAuthenticationToken
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class); // Cria um objeto de UsernamePasswordAuthenticationToken a partir do login e a senha, e passa o objeto para o provides verificando se ele suporta o tipo de autenticação.
    }
}
