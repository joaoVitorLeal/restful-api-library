package io.github.joaoVitorLeal.libraryapi.controllers;

import io.github.joaoVitorLeal.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String showHomePage(Authentication authentication) {
        if (authentication instanceof CustomAuthentication customAuth) {
            System.out.println(customAuth.getUser());
        }
        return "Hello, " + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code) {
        return "Your Authorization Code: " + code;
    }
}