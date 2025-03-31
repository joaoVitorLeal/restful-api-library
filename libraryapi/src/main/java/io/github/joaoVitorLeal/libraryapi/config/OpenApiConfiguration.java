package io.github.joaoVitorLeal.libraryapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library API",
                version = "1.0",
                description = """ 
                        ***📚 Library Management API RESTful*** \s
                        
                        **Core Features:** 📖 CRUD | 🔐 JWT Auth | 👥 Role-based Access | 🔍 Advanced Search \s
                        
                        **🛡️ Security:** `Authorization: Bearer {token}` | 🔑 Token via `POST /oauth/token` (client_credentials) \s
                        
                        **👮 Roles:** MANAGER: Full access | OPERATOR: Full operations in Book, and constraints in Author \s
                        
                        **⚠️ Key Constraints:** Unique ISBN | Post-2020 books require price | Authors with books cannot be deleted \s
                        """,
                contact = @Contact(
                        name = "João Leal",
                        email = "joaoleal98@outlook.com",
                        url = "https://github.com/joaoVitorLeal"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}

