package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.ApiResponseDTO;
import com.snkuni.sankuni.dtos.AsistenciaRequestDTO;
import com.snkuni.sankuni.services.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<ApiResponseDTO> registrar(@Valid @RequestBody AsistenciaRequestDTO request) {
        asistenciaService.registrarAsistencia(request);
        return ResponseEntity.ok(new ApiResponseDTO(true, "Asistencia registrada correctamente", null));
    }
}