package br.com.tcc.exceptions;

import br.com.tcc.exception.ConsistirException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TratarMensagemExceptions {

    public Response getErrorResponse(MethodArgumentNotValidException ex, HttpStatusCode status, List<MessageError> errors) {
        return new Response("Requisição possui campos inválidos", status.value(),
                ex.getBindingResult().getObjectName(), errors);
    }

    public Response getErrorResponse(Exception ex, HttpStatus status, List<MessageError> errors) {
        return new Response("Erro interno", status.value(), null, errors);
    }

    public Response getErrorResponse(ConsistirException ex, HttpStatus status, List<MessageError> errors) {
        return new Response("Requisição possui campos inválidos", status.value(), null, errors);
    }

    public List<MessageError> getErrors(MethodArgumentNotValidException ex) {
        if(ex.getBindingResult().getFieldErrors().isEmpty()) {
            return getErrosDefaultMessage(ex);
        }
        else {
            return getErrosCustomMessage(ex);
        }
    }

    private List<MessageError> getErrosDefaultMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(error -> new MessageError("", error.getDefaultMessage(), ""))
                .collect(Collectors.toList());
    }

    private List<MessageError> getErrosCustomMessage(MethodArgumentNotValidException ex)  {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new MessageError(error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .collect(Collectors.toList());
    }

    public List<MessageError> getErrors(Exception ex) {
        if (ex instanceof ConsistirException) {
            List<String> erros = ((ConsistirException) ex).getErroSimples();

            return erros.stream()
                    .filter(erro -> erro != null && !erro.isEmpty())
                    .map(erro -> new MessageError(null, erro, null))
                    .collect(Collectors.toList());
        }

        return Collections.singletonList(new MessageError(null, ex.getMessage(), null));
    }

}
