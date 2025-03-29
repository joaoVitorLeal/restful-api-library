package io.github.joaoVitorLeal.libraryapi.security.utils;

import io.github.joaoVitorLeal.libraryapi.security.LoginSocialSuccessHandler;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class to generate random passwords for social login users.
 * This password is generated when a new user is registered via OAuth2 authentication,
 * ensuring the user has a default password set during the first login process.
 *
 * Usage example:
 * @see LoginSocialSuccessHandler
 */
public class PasswordGenerator {

    public static String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
