package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.DocenteDTO;
import com.snkuni.sankuni.services.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;

    // Obtiene el perfil completo del docente usando el ID de su usuario
  // Agrégale ("usuarioId") adentro del PathVariable
    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<DocenteDTO> obtenerPerfilUsuario(@PathVariable("usuarioId") UUID usuarioId) {
        return ResponseEntity.ok(docenteService.obtenerPerfilPorUsuario(usuarioId));
    }
}