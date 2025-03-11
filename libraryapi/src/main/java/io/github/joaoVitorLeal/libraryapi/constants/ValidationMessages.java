package io.github.joaoVitorLeal.libraryapi.constants;

public class ValidationMessages {

    public static final String  REQUIRED_FIELD_MESSAGE = "Campo obrigatório.";
    public static final String FIELD_SIZE_MESSAGE = "Campo fora do tamanho padrão.";
    public static final String INVALID_DATE_MESSAGE = "Data inválida.";
    public static final String MIN_PASSWORD_LENGTH_MESSAGE = "A senha deve possuir no mínimo 6 caracteres.";
    public static final String INVALID_EMAIL_MESSAGE = "Email inválido.";
    public static final String INVALID_ISBN_MESSAGE = "ISBN inválido.";

    private ValidationMessages() {
        throw  new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
