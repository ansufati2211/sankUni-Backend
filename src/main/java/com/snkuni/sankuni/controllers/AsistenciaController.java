package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.ApiResponseDTO;
import com.snkuni.sankuni.dtos.AsistenciaDTO; // <-- Importación
import com.snkuni.sankuni.dtos.AsistenciaRequestDTO;
import com.snkuni.sankuni.services.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<ApiResponseDTO> registrar(@Valid @RequestBody AsistenciaRequestDTO request) {
        asistenciaService.registrarAsistencia(request);
        return new ResponseEntity<>(new ApiResponseDTO(true, "Asistencia registrada correctamente", null), HttpStatus.CREATED);
    }

    @GetMapping("/seccion/{idSeccion}/fecha/{fecha}")
    public ResponseEntity<List<AsistenciaDTO>> obtenerAsistencia(
            @PathVariable Long idSeccion,
            @PathVariable String fecha) {
        return ResponseEntity.ok(asistenciaService.obtenerPorSeccionYFecha(idSeccion, LocalDate.parse(fecha)));
    }
}