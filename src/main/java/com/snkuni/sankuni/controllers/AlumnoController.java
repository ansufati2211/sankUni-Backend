package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.AlumnoDTO;
import com.snkuni.sankuni.services.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    // SOLUCIÓN AL FALLO: GET http://localhost:8080/api/v1/alumnos
    @GetMapping
    public ResponseEntity<List<AlumnoDTO>> listarTodos() {
        return ResponseEntity.ok(alumnoService.listarTodos());
    }

    // SOLUCIÓN AL FALLO: GET http://localhost:8080/api/v1/alumnos/1
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(alumnoService.obtenerPorId(id));
    }

    // Mantiene la búsqueda por usuario_id para el Login/Perfil
    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<AlumnoDTO> obtenerPerfilUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(alumnoService.obtenerPerfilPorUsuario(usuarioId));
    }
}