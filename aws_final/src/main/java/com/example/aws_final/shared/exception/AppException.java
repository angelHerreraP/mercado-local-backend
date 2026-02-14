package com.example.aws_final.shared.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{ // lo llevamos a runttime para que no crashee el programa completo
    public final ErrorCodes errorCode;



    public AppException(ErrorCodes errorCode){
        // Le pasamos el mensaje del Enum al padre (RuntimeException)
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
