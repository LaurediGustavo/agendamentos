package br.com.tcc.exceptions;


import jakarta.ws.rs.Produces;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<MessageError> errors = getErrors(ex);
        Response errorResponse = getErrorResponse(ex, ex.getStatusCode(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private Response getErrorResponse(MethodArgumentNotValidException ex, HttpStatusCode status, List<MessageError> errors) {
        return new Response("Requisição possui campos inválidos", status.value(),
                ex.getBindingResult().getObjectName(), errors);
    }

    private List<MessageError> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new MessageError(error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .collect(Collectors.toList());
    }

}
