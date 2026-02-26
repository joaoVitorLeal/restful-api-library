package io.github.jvlealc.libraryapi.constants;

public class ValidationMessages {

    public static final String REQUIRED_FIELD_MESSAGE = "This field is required.";
    public static final String FIELD_SIZE_MESSAGE = "Field exceeds the standard size.";
    public static final String INVALID_DATE_MESSAGE = "Invalid date.";
    public static final String MIN_PASSWORD_LENGTH_MESSAGE = "Password must be at least 6 characters long.";
    public static final String INVALID_EMAIL_MESSAGE = "Invalid email address.";
    public static final String INVALID_ISBN_MESSAGE = "Invalid ISBN.";


    private ValidationMessages() {
        throw  new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
