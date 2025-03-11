package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // Controller para páginas Web
public class LoginViewController {

    @GetMapping("/login")
    public String showLoginPage() { // Utilizando String para indicar qual página HTML ele deve ir quando esta requisição(GET) for chamada
        return "login";
    }

    @GetMapping("/")
    @ResponseBody // Ao utilizar esta @annotation, retornará a resposta diretamente no corpo da requisição, em vez de renderizar uma página HTML
    public String showHomePage(Authentication authentication) {
        if(authentication instanceof CustomAuthentication customAuth) {
            System.out.println(customAuth.getUser());
        }
        return "Olá " + authentication.getName();
    }

}
