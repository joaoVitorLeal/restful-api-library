package io.github.joaoVitorLeal.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // csrf() -> utilizado para proteção aplicação web. Para que a aplicação consiga fazer as requisições de forma autenticada ela deve enviar um Token CSRF de autenticação. para garantir que a página que enviou a requisição é da própria aplicação.  impede que páginas de outros sistemas consigam fazer requisição para nossa aplicação.
                .httpBasic(Customizer.withDefaults()) // httpBasic() -> utilizado para outras aplicações se comunicarem com esta. Exemplo Postman Agent, sendo necessário informar user/password.
                .formLogin(form -> form
                        .loginPage("/login") // Customizando qual é a página de login da aplicação.
                ) // formLogin() -> utilizado em Browser para usuários da aplicação, fromulário de login: user/password.
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll(); // Permite que qualquer tipo de usuário acesse a página de Login (URI)
                    authorize.requestMatchers("/authors/**").hasRole("ADMIN");
                    authorize.requestMatchers("/books/**").hasAnyRole("USER", "ADMIN");
                    authorize.anyRequest().authenticated(); // Exige autenticação para qualquer requisição que não seja as que foram mapeadas com requestMatchers().
                    // anyRequest() - Deve ficar por último pois qualquer regra após o anyRequest() será IGNORADA!

                    ///* MAIS EXEMPLOS: Realizando controle de requisições utilizando Roles e Athority dos usuários indicando quais Http Methods (requisições) podem ser feitas por tipo de usuário(USER ou ADMIN) */
                    //authorize.requestMatchers(HttpMethod.POST, "/authors/**").hasRole("ADMIN");
                    //authorize.requestMatchers(HttpMethod.POST, "/authors/**").hasAuthority("CADASTRAR_AUTOR");
                    //authorize.requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN");
                    //authorize.requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN");
                    //authorize.requestMatchers(HttpMethod.GET, "/authors/**").hasAnyRole("USER", "ADMIN");
                })
                .build();
    }

    // Codificador de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Configura o BCrypt com fator de custo 10, isso significa que o algoritmo realizará 2^10 (1024) iterações para gerar o hash.
    }

    /*
     * Neste caso iremos criar usuários em memória e
     * não utilizar usuários de uma base de dados.
     * */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        /* Criando instâncias de usuários em memória */
        UserDetails user1 = User.builder()
                .username("usuario")
                .password(encoder.encode("123"))
                .roles("USER")
                .build();

        UserDetails user2 = User.builder()
                .username("admin")
                .password(encoder.encode("321"))
                .roles("ADMIN")
                .build();


        return new InMemoryUserDetailsManager(user1, user2);
    }
}
//.formLogin(configurer -> configurer.loginPage("/login.html").successForwardUrl("/home.html")) // loginPage() -> indica a url de uma página html de login && successForwardUrl() -> redireciona para página caso o login seja bem sucedido.
