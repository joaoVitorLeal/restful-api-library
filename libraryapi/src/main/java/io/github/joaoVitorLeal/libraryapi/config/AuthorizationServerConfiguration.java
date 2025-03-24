package io.github.joaoVitorLeal.libraryapi.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.joaoVitorLeal.libraryapi.security.CustomAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/*
* Authorization Server personalizada para esta aplicação
* */
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    /*
    * Configuração para habilíta o Authorization Server
    * */
    @Bean
    @Order(1) // Define a ordem que este filter chain vai ficar. Neste caso ele é o principal (1) da aplicação.
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); // Habilitando o AuthorizationServer na aplicação

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // Habilitando o OpenIdConnect (Plugin do oauth2 que permite com que com Token obtido, obter detalhes como quem foi que o gerou, quem é o usuário)

        http.oauth2ResourceServer(oauth2RS -> oauth2RS.jwt(Customizer.withDefaults())); // Habilita o Token JWT e verifica o Token foi gerado por este Authorization Server para quem usá-lo

        http.formLogin(configurer -> configurer.loginPage("/login"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // Formato de Token
                // access_token: token utilizado nas requisições
                .accessTokenTimeToLive(Duration.ofMinutes(60)) // Tempo de duração do Token
                // refresh_token: token para renovar o access_token
                .refreshTokenTimeToLive(Duration.ofMinutes(90)) // O tempo de duração do Token é estendido
                .build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    // JWK - gera tokens JWK (JSON Web Key). Representação em JSON de uma chave criptográfica usadas em processos de autenticação e assinatura digital. Necessário quando se trabalha com JWT para que o JWK assine o Token.
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        RSAKey rsaKey = generateRSAKey(); // Gerando chave RSA, utiliza criptografia assimétrica. É um méto-do de criptografia que consiste em duas chaves, uma pública que serve para criptografar os dados, e outra privada utilizada para descriptografar dados que foram criptografados com a chave pública. Somente o Authorization Server conhece a chave privada.
        JWKSet jwkSet = new JWKSet(rsaKey); // recebe uma jwkKey
        return new ImmutableJWKSet<>(jwkSet);
    }

    // Méto-do auxiliar para gerar chaves RSA
    private RSAKey generateRSAKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Inicializando chave com 2048 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); // Gerando o par de chaves

        RSAPublicKey publicKey =  (RSAPublicKey) keyPair.getPublic(); // Obtendo RSA público
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // Obtendo RSA privado

        return new RSAKey
                .Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /* Conhecendo e customizando endpoints do Authorization Server */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                // Obter token
                .tokenEndpoint("/oauth2/token")
                // Utilizado para consultar status do token
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                // Revoga o token
                .tokenRevocationEndpoint("/oauth2/revoke")
                // Authorization endpoint. Que utilizamos no AUTHORIZATION_CODE
                .authorizationEndpoint("/oauth2/authorize")
                // Obter informações do usuário OPEN ID CONNECT
                .oidcUserInfoEndpoint("/oauth2/userinfo")
                // Obter chave pública para verificar a assinatura do token
                .jwkSetEndpoint("/oauth2/jwks")
                // Logout
                .oidcUserInfoEndpoint("/oauth2/logout")
                .build();
    }

    /* Adiciona informações customizadas no Token JWT */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            var principal = context.getPrincipal();

            if (principal instanceof CustomAuthentication authentication) {
                OAuth2TokenType tokenType = context.getTokenType();

                if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
                    Collection<GrantedAuthority> authorities = authentication.getAuthorities();
                    List<String> authoritiesList = authorities.stream().map(GrantedAuthority::getAuthority).toList();
                    context
                            .getClaims()
                            .claim("authorities", authoritiesList)
                            .claim("email", authentication.getUser().getEmail())
//                            .claim("birthDate", authentication.getUser().getBirthDate())
                            .build();
                }
            }
        };
    }
}
