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
 * Classe que implementa a interface Authentication do Spring Security,
 * representando a autenticação personalizada do usuário com suas permissões (roles) e outros detalhes.
 * Essa classe é usada para FORNECER informações sobre o usuário autenticado no sistema.
 */
@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final User user;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        // Transforma a lista de roles (strings) do usuário em uma lista de GrantedAuthority
        // Cada role será convertida em uma instância de SimpleGrantedAuthority
        return this.user
                .getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /// Utilizado para retornar detalhes do usuário. Ex.: nome, cpf, departamento, salário, etc...
    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    ///  ATENÇÃO:
    /// Por padrão o retorno é false,
    ///  sendo necessário alterar para true para podemos realizar o log (autenticação)
    @Override
    public boolean isAuthenticated() {
        return true; // Por padrão é false
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    // Retorna o nome de usuário (username) para o processo de autenticação.
    @Override
    public String getName() {
        return user.getUsername();
    }
}
