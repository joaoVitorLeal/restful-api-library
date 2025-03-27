package io.github.joaoVitorLeal.libraryapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// DOC: configuração da documentação da Library API (SWAGGER)
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library API",
                version = "v1",
                contact = @Contact(
                        name = "João Leal",
                        email = "joaoleal98@outlook.com",
                        url = "libraryapi.com"
                )
        ),
        // Adiciona o Botão de autenticação
        security = {
                @SecurityRequirement(name = "bearerAuth") // Requisitando segurança do tipo bearerAuth
        }
)
@SecurityScheme( // Definindo a bearerAuth para o @SecurityRequirement()
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP, // Colocaremos o Token e ele irá enviar via HTTP
        bearerFormat = "JWT", // Formato do bearer
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER // Adiciona o Token no Header da requisição
)
public class OpenApiConfiguration {
}
