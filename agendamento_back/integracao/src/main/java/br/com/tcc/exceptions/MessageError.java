package br.com.tcc.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageError {

    private final String field;

    private final String message;

    private final Object parameter;

}
