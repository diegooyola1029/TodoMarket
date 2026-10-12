package com.todomarket.inventory.api;

import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarSolicitudInvalida(IllegalArgumentException excepcion) {
        String mensaje = excepcion.getMessage() == null ? "Solicitud no válida." : excepcion.getMessage();
        String normalizado = mensaje.toLowerCase(Locale.ROOT);
        HttpStatus estado = normalizado.contains("no encontrado")
                ? HttpStatus.NOT_FOUND
                : normalizado.contains("ya existe") ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(estado).body(new ApiError(mensaje, LocalDateTime.now()));
    }
}
