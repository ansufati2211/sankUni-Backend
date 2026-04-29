package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.DocenteDTO;
import com.snkuni.sankuni.services.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;

    // SOLUCIÓN AL FALLO: GET http://localhost:8080/api/v1/docentes
    @GetMapping
    public ResponseEntity<List<DocenteDTO>> listarTodos() {
        return ResponseEntity.ok(docenteService.listarTodos());
    }

    // CORREGIDO: Uso de Long para el ID
    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(docenteService.obtenerPorId(id));
    }

    // Perfil vinculado al Usuario (Login)
    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<DocenteDTO> obtenerPerfilPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(docenteService.obtenerPerfilPorUsuario(usuarioId));
    }
}