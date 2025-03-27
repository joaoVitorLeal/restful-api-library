package io.github.joaoVitorLeal.libraryapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // Habilitando MVC na aplicação, possibilitando criar paginas web
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) { // Registrando a View
       registry.addViewController("/login").setViewName("login"); // Registando a página Web  na URL "/login" e nomeando a página como "login"
       registry.setOrder(Ordered.HIGHEST_PRECEDENCE); // Ordenando registro com alta precedência
    }
}
