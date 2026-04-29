package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.AlumnoDTO;
import com.snkuni.sankuni.services.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    // Obtiene el perfil completo del alumno usando el ID de su usuario (Ideal para el Login)
  // Agrégale ("usuarioId") adentro del PathVariable
    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<AlumnoDTO> obtenerPerfilUsuario(@PathVariable("usuarioId") UUID usuarioId) {
        return ResponseEntity.ok(alumnoService.obtenerPerfilPorUsuario(usuarioId));
    }
}