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

    @GetMapping
    public ResponseEntity<List<AlumnoDTO>> listarTodos() {
        return ResponseEntity.ok(alumnoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(alumnoService.obtenerPorId(id));
    }

    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<AlumnoDTO> obtenerPerfilUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(alumnoService.obtenerPerfilPorUsuario(usuarioId));
    }

    // NUEVO: Búsqueda por DNI (Coordinador)
    @GetMapping("/dni/{dni}")
    public ResponseEntity<AlumnoDTO> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(alumnoService.buscarPorDni(dni));
    }
}