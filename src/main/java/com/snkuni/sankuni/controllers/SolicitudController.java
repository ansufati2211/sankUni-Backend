package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.SolicitudDTO;
import com.snkuni.sankuni.dtos.SolicitudRequestDTO;
import com.snkuni.sankuni.services.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudDTO> crear(@Valid @RequestBody SolicitudRequestDTO request) {
        return ResponseEntity.ok(solicitudService.crearSolicitud(request));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<SolicitudDTO>> listarMisSolicitudes(@PathVariable("id") Long id) {
        return ResponseEntity.ok(solicitudService.listarMisSolicitudes(id));
    }

    // NUEVO: Ver todos los trámites (Admin)
    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> listarTodas() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    // NUEVO: Responder trámite (Admin)
    @PutMapping("/{id}/responder")
    public ResponseEntity<SolicitudDTO> responderSolicitud(
            @PathVariable Long id, 
            @RequestParam String estado, 
            @RequestParam String observacion) {
        return ResponseEntity.ok(solicitudService.responderSolicitud(id, estado, observacion));
    }
}