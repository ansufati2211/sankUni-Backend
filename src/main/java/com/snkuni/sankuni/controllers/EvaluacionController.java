package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.EvaluacionDTO;
import com.snkuni.sankuni.services.EvaluacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {
    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<EvaluacionDTO> crear(@Valid @RequestBody EvaluacionDTO dto) {
        return ResponseEntity.ok(evaluacionService.crearEvaluacion(dto));
    }

 // Agrégale ("id") adentro del PathVariable
    @GetMapping("/seccion/{id}")
    public ResponseEntity<List<EvaluacionDTO>> listar(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(evaluacionService.listarPorSeccion(id));
    }
}