package io.github.joaoVitorLeal.libraryapi.controllers.common;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ErrorResponse;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice // Capturar Exceptions trata e retorna uma resposta REST
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Realiza a captura e lança a exception
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Mapeia e inclui no retorno do métod0 um Status HTTP do ResponseEntity
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<ValidationError> errorList = fieldErrors
                .stream()
                .map(fe -> new ValidationError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação.", errorList);
    }
}
