package com.snkuni.sankuni.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Errores de Búsqueda (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 2. Manejo de Reglas de Negocio Rotas (400)
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessLogicException(BusinessLogicException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 3. Manejo de Errores de Validación de JSON (Ej. @NotNull, @Email en los DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errores = new HashMap<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errores", errores);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 4. Manejo de Errores Críticos (Base de datos PostgreSQL, Stored Procedures, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        String message = ex.getMessage();
        
        // Limpiamos los mensajes feos del motor SQL
        if (message != null && message.contains("Error:")) {
            message = message.substring(message.indexOf("Error:"));
        } else if (message != null && (message.contains("duplicate key value") || message.contains("llave duplicada"))) {
            message = "Operación rechazada: Ya existe un registro con esos datos exactos en la base de datos.";
        } else {
            message = "Ocurrió un error interno en el servidor.";
            ex.printStackTrace(); // Imprime en consola para que puedas debugear
        }
        
        return buildResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método auxiliar para no repetir código al armar el JSON de respuesta
    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", message);
        return new ResponseEntity<>(response, status);
    }
}