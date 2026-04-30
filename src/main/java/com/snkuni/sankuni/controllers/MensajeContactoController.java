package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.MensajeContactoDTO;
import com.snkuni.sankuni.services.MensajeContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mensajes")
@RequiredArgsConstructor
public class MensajeContactoController {

    private final MensajeContactoService mensajeService;

    // POST /api/v1/mensajes -> Formulario de la web
    @PostMapping
    public ResponseEntity<MensajeContactoDTO> guardarMensaje(@RequestBody MensajeContactoDTO dto) {
        return new ResponseEntity<>(mensajeService.guardarMensaje(dto), HttpStatus.CREATED);
    }

    // GET /api/v1/mensajes/pendientes -> Para el panel del Admin/Coordinador
    @GetMapping("/pendientes")
    public ResponseEntity<List<MensajeContactoDTO>> listarPendientes() {
        return ResponseEntity.ok(mensajeService.listarPendientes());
    }
}