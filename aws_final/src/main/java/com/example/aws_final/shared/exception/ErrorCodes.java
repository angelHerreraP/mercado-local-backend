package com.example.aws_final.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCodes {

    // Los 400
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, "Los datos son invalidos."),
    USER_EXISTS(HttpStatus.CONFLICT, "El usuario ya se encuentra registrado."),
    EMAIL_EXISTS(HttpStatus.CONFLICT, "El email ya se encuentra en uso."),
    EMPTY_STOCK(HttpStatus.CONFLICT, "El Stock de este producto esta agotado o no es suficiente."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "El recurso solicitado no existe."),
    EMPTY_PRODUCTS(HttpStatus.NOT_ACCEPTABLE, "La lista de productos no puede estar vacia"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "No tiene permisos para realizar esta accion. "),

    //Los 500, para debuggear rapido
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno, intenta mas tarde.");

    @Getter
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCodes(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
