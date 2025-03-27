package io.github.joaoVitorLeal.libraryapi.controllers.common;

import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ErrorResponseDTO;
import io.github.joaoVitorLeal.libraryapi.controllers.dtos.ValidationErrorDTO;
import io.github.joaoVitorLeal.libraryapi.exceptions.BusinessRuleException;
import io.github.joaoVitorLeal.libraryapi.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.libraryapi.exceptions.OperationNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice // Capturar Exceptions trata e retorna uma resposta REST
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Realiza a captura e lança a exception com o objeto Exception que iremos receber para tratar
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Mapeia e inclui no retorno do métod0 um Status HTTP do ResponseEntity
    public ErrorResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ValidationErrorDTO> errorList = fieldErrors
                .stream()
                .map(fe -> new ValidationErrorDTO(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error:", errorList);
    }

    @ExceptionHandler(DuplicateRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleDuplicateRegistrationException (DuplicateRegistrationException e) {
        return ErrorResponseDTO.conflict(e.getMessage());
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleOperationNotPermittedException(OperationNotPermittedException e) {
        return ErrorResponseDTO.standardResponse(e.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponseDTO handleBusinessRuleException(BusinessRuleException e) {
        return new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error:",
                List.of(new ValidationErrorDTO(e.getField(), e.getMessage()))
        );

    }

    /*
     * Tratando o caso de uso de acesso/operação não autorizada
     * por um determinado grupo de usuários.
     * */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Access denied.",
                List.of()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleUntreatedErrors(RuntimeException e) {
        log.error("Unexpected error - Error details:", e); // Gera um log de erro interno do servidor com maiores detalhes.
        return new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please contact the administration.",
                List.of());
    }
}
