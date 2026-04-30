package com.snkuni.sankuni.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
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
        response.put("message", "Error de validación en los campos enviados."); 
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 4. Manejo Inteligente de Integridad de Base de Datos (UNIQUE, CHECK)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        // Extraemos el mensaje real y profundo de la base de datos PostgreSQL
        String mensajeDB = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";

        // Si el error es por un DNI o Correo repetido (Violación de UNIQUE)
        if (mensajeDB.toLowerCase().contains("duplicate") || mensajeDB.toLowerCase().contains("llave duplicada")) {
            return buildResponse("Error: El DNI o Correo Electrónico ya se encuentra registrado en el sistema.", HttpStatus.CONFLICT);
        } 
        // Si el error es por el horario de la sección (Violación de CHECK)
        else if (mensajeDB.toLowerCase().contains("check_horario")) {
            return buildResponse("Error de horario: La hora de inicio debe ser menor a la hora de fin.", HttpStatus.BAD_REQUEST);
        }

        // Para cualquier otro error de integridad desconocido
        return buildResponse("Ocurrió un error de integridad en la base de datos.", HttpStatus.BAD_REQUEST);
    }

    // 5. Manejo de Errores Críticos (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        String message = ex.getMessage();
        
        // Limpiamos los mensajes feos del motor SQL
        if (message != null && message.contains("Error:")) {
            message = message.substring(message.indexOf("Error:"));
        } else {
            message = "Ocurrió un error interno en el servidor.";
            ex.printStackTrace(); // Imprime en consola para debugear
        }
        
        return buildResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método auxiliar unificado para armar el JSON de respuesta
    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("message", message); // Para que lo lea el JS (data.message)
        response.put("error", message);   // Por si algún fetch antiguo usa data.error
        return new ResponseEntity<>(response, status);
    }
}