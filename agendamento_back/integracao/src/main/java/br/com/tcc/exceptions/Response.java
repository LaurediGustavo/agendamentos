package br.com.tcc.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Response {

    private final String message;

    private final int code;

    private final String objectName;

    private final List<MessageError> errors;

}
