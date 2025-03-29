package io.github.joaoVitorLeal.libraryapi.exceptions;

import lombok.Getter;

/**
 * Thrown when a post-2020 book has no price defined.
 * This business rule ensures recent books have a mandatory price.
 */
public class BusinessRuleException extends RuntimeException{

    @Getter
    private String field;

    public BusinessRuleException(String field, String message) {
        super(message);
        this.field = field;
    }
}
