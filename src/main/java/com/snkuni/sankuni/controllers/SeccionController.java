package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.SeccionDTO;
import com.snkuni.sankuni.services.SeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secciones")
@RequiredArgsConstructor
public class SeccionController {

    private final SeccionService seccionService;

    // Devuelve la lista de secciones (horarios) filtradas por un ciclo específico (Ej: "2026-I")
  // Agrégale ("ciclo") adentro del PathVariable
    @GetMapping("/ciclo/{ciclo}")
    public ResponseEntity<List<SeccionDTO>> listarPorCiclo(@PathVariable("ciclo") String ciclo) {
        return ResponseEntity.ok(seccionService.listarPorCiclo(ciclo));
    }
}