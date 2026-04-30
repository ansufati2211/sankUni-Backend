package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.AlertaAcademicaDTO;
import com.snkuni.sankuni.dtos.ApiResponseDTO;
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

    // GET /api/v1/alertas/pendientes -> Muestra docentes que no llenan notas o asistencia
    @GetMapping("/pendientes")
    public ResponseEntity<List<AlertaAcademicaDTO>> listarPendientes() {
        return ResponseEntity.ok(alertaService.listarPendientes());
    }

    @PutMapping("/{id}/resolver")
public ResponseEntity<ApiResponseDTO> resolverAlerta(@PathVariable Long id) {
    alertaService.resolverAlerta(id);
    return ResponseEntity.ok(new ApiResponseDTO(true, "Alerta resuelta", null));
}
}