package io.github.joaoVitorLeal.libraryapi.config;

import io.github.joaoVitorLeal.libraryapi.security.JwtCustomAuthenticationFilter;
import io.github.joaoVitorLeal.libraryapi.security.LoginSocialSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

// RESOURCE SERVER DA APLICAÇÂO //
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // Habilita o uso de operações de segurança, como roles, authority, etc, nos Controllers da aplicação
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler successHandler, JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable).httpBasic(Customizer.withDefaults()).formLogin(form -> form.loginPage("/login")).authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/users/**").permitAll();

                    authorize.anyRequest().authenticated(); // Caso não seja configurado uma regra de acesso para determinar as roles, ele cairá nesta regra. E Apesar de estarem autenticado, não terá autorização para realizar requisições
                })
                // Habilita o login via OAuth2 (não configurado, mas habilitado por padrão)
                .oauth2Login(oauth2 -> {
                    oauth2.loginPage("/login").successHandler(successHandler); // Passando a autenticação do Google já configurada para nossa aplicação em LoginSocialSuccessHandler.class
                }).oauth2ResourceServer(oauth2RS -> oauth2RS.jwt(Customizer.withDefaults()))  // Habilitando JTW para validar usuário na aplicação. Será o Token Padrão.
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    // CONFIGURA O PREFIXO SCOPE NO SECURITY
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() { // Utilizando para remover o prefíxo 'ROLE_' do Security. Também é possível customizar prefíxos.
        return new GrantedAuthorityDefaults("");
    }

    // CONFIGURA, NO TOKEN JWT, O PREFIXO SCOPE
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }

    // (Foi transferido para o AuthorizationServerConfiguration.class) Codificador de senhas
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10);

//    }

//    /**
//     * @Deprecated -
//     * Provia a autenticação através do UserDetails.
//     * Agora sendo realizada no @see CustomAuthenticationProvider
//     */
//    @Bean ** ao comentar essa annotation esse métod0 deixa de ser 'visível'/gerenciado pela aplicação.
//    @Deprecated
//    protected UserDetailsService userDetailsService(UserService userService) {
//        return new CustomUserDetailsService(userService);

//    }
}
