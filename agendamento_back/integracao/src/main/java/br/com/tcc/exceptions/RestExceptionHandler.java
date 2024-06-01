package br.com.tcc.exceptions;


import br.com.tcc.exception.ConsistirException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private TratarMensagemExceptions tratarMensagemExceptions;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<MessageError> errors = tratarMensagemExceptions.getErrors(ex);
        Response errorResponse = tratarMensagemExceptions.getErrorResponse(ex, ex.getStatusCode(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConsistirException.class)
    public ResponseEntity<?> consistirException(ConsistirException ex) {
        List<MessageError> errors = tratarMensagemExceptions.getErrors(ex);
        Response errorResponse = tratarMensagemExceptions.getErrorResponse(ex, HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, DisabledException.class, BadCredentialsException.class})
    public ResponseEntity<?> handleInternalServerError(Exception ex) {
        List<MessageError> errors = tratarMensagemExceptions.getErrors(ex);
        Response errorResponse = tratarMensagemExceptions.getErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}
