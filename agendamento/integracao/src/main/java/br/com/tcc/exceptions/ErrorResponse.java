package br.com.tcc.exceptions;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ErrorResponse {

    private final String message;

    private final int code;

    private final String objectName;

    private final List<ErrorObject> errors;

}
