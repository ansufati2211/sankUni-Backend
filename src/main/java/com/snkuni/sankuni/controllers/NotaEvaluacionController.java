package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.NotaEvaluacionDTO;
import com.snkuni.sankuni.services.NotaEvaluacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notas")
@RequiredArgsConstructor
public class NotaEvaluacionController {
    private final NotaEvaluacionService notaService;

    @PostMapping("/registrar")
    public ResponseEntity<NotaEvaluacionDTO> registrar(@Valid @RequestBody NotaEvaluacionDTO dto) {
        return ResponseEntity.ok(notaService.registrarNota(dto));
    }
}