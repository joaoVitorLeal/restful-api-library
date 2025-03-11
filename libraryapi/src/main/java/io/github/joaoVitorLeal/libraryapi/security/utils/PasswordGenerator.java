package io.github.joaoVitorLeal.libraryapi.security.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordGenerator {

    // Méto-do para gerar uma senha aleatória segura
    public static String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
