package io.github.joaoVitorLeal.libraryapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Controller para páginas Web
public class LoginViewController {

    @GetMapping("/login")
    public String showLoginPage() { // Utilizando String para indicar qual página ele deve ir quando esta requisição(GET) for chamada
        return "login";
    }
}
