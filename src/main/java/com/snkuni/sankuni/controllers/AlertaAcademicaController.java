package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.AlertaAcademicaDTO;
import com.snkuni.sankuni.dtos.ApiResponseDTO; // Asegúrate de tener este import
import com.snkuni.sankuni.services.AlertaAcademicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alertas")
@RequiredArgsConstructor
public class AlertaAcademicaController {

    private final AlertaAcademicaService alertaService;

    @GetMapping("/pendientes")
    public ResponseEntity<List<AlertaAcademicaDTO>> listarPendientes() {
        return ResponseEntity.ok(alertaService.listarPendientes());
    }

    // 🚀 ESTE ES EL BLOQUE QUE TE FALTABA
    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<List<AlertaAcademicaDTO>> listarAlertasDocente(@PathVariable Long idDocente) {
        return ResponseEntity.ok(alertaService.listarPorDocente(idDocente));
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<ApiResponseDTO> resolverAlerta(@PathVariable Long id) {
        alertaService.resolverAlerta(id);
        return ResponseEntity.ok(new ApiResponseDTO(true, "Alerta resuelta", null));
    }
}