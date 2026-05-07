package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.NotaEvaluacionDTO;
import com.snkuni.sankuni.services.NotaEvaluacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
@RequiredArgsConstructor
public class NotaEvaluacionController {

    private final NotaEvaluacionService notaService;

    // Le quitamos el @Valid para que no choque al recibir la nota
    @PostMapping("/registrar")
    public ResponseEntity<NotaEvaluacionDTO> registrar(@RequestBody NotaEvaluacionDTO dto) {
        return ResponseEntity.ok(notaService.registrarNota(dto));
    }

    @GetMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<List<NotaEvaluacionDTO>> obtenerNotas(@PathVariable Long idEvaluacion) {
        return ResponseEntity.ok(notaService.obtenerNotasPorEvaluacion(idEvaluacion));
    }
}