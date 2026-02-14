package com.example.aws_final.shared.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice // Esto le dice a Spring, yo manejo errores
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex){
        ErrorCodes code = ex.getErrorCode();

        // Creamos el objeto que definiste antes con Instant.now()

        ErrorResponse response = new ErrorResponse(
                code.getHttpStatus().value(),
                code.getMessage()
        );

        // Devolvemos el JSON con el código HTTP correcto (409, 404, etc.)
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    // 2. Errores de Validación (Cuando fallan las anotaciones @Valid en el DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Juntamos todos los errores de los campos en un solo string
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Datos inválidos: " + errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 3. Error Maestro (El seguro de vida para el 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // IMPORTANTE: Aquí sí logueamos el error real para nosotros verlo en CloudWatch
        // Pero al cliente le damos un mensaje genérico por seguridad forense.
        System.err.println("CRITICAL ERROR: " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado en el servidor de la farmacia."
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
