package io.github.joaoVitorLeal.libraryapi.security;

import io.github.joaoVitorLeal.libraryapi.models.User;
import io.github.joaoVitorLeal.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

    /*
     * Devido à implementação de uma autenticação customizada em CustomAuthentication.class,
     * foi necessário reconfigurar o SecurityService.class, e este méto-do tinha como função obter o usuário
     * do local correto. Como a aplicação deixou de utilizar UserDetails, este méto-do tornou-se obsoleto.
     *
     * @Deprecated
     * public User getAuthenticatedUser() {
     *         // Obtém o objeto de autenticação do contexto de segurança
     *         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     *
     *         // Recupera os detalhes do usuário autenticado a partir do objeto de autenticação
     *         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
     *
     *         // Obtém o nome de usuário do objeto UserDetails
     *         String username = userDetails.getUsername();
     *
     *         // Utiliza o serviço de usuário para buscar o usuário no banco de dados pelo nome de usuário
     *         return userService.findByUsername(username);
     *     }
     * */
}
