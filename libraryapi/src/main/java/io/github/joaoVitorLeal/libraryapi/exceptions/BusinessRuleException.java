package io.github.joaoVitorLeal.libraryapi.exceptions;

import lombok.Getter;

/**
 * Exceção lançada quando um livro publicado após 2020 não possui preço informado.
 * Essa regra de negócio garante que livros recentes tenham um valor obrigatório.
 */
public class BusinessRuleException extends RuntimeException{

    @Getter
    private String field;

    public BusinessRuleException(String field, String message) {
        super(message);
        this.field = field;
    }
}
