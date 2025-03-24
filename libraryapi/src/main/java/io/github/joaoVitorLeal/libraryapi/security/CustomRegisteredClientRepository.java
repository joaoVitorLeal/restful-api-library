package io.github.joaoVitorLeal.libraryapi.security;

import io.github.joaoVitorLeal.libraryapi.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;
    private final TokenSettings tokenSettings; // tokenSettings customizado em AuthorizationServerConfiguration.class
    private final ClientSettings clientSettings; // clientSettings customizado em AuthorizationServerConfiguration.class

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client = clientService.findByClientId(clientId).orElse(null);

        if(client == null) {
            return null;
        }

        return RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectURI())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // Define o méto-do de autenticação do Cliente
                // grant_types: maneiras que um sistema externo possui para obter acesso a um Access Token para consumo da API
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // De usuários para aplicação
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // De aplicação para aplicação.  Não utilizar refresh_token com CLIENT_CREDENTIALS
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // Renovar o Access Token (JWT), em casos em que o token de usuário foi expirado, mas ele ainda continua realizando operações na aplicação. É um Token Opaco.
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
