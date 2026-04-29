package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.SolicitudDTO;
import com.snkuni.sankuni.dtos.SolicitudRequestDTO;
import com.snkuni.sankuni.services.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {
    private final SolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudDTO> crear(@Valid @RequestBody SolicitudRequestDTO request) {
        return ResponseEntity.ok(solicitudService.crearSolicitud(request));
    }

   // Agrégale ("id") adentro del PathVariable
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<SolicitudDTO>> listar(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(solicitudService.listarMisSolicitudes(id));
    }
}