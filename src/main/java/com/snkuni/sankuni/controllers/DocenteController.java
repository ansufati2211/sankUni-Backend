package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.DocenteDTO;
import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.services.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;

    @PostMapping("/registro")
    public ResponseEntity<DocenteDTO> registrarDocente(@RequestBody Map<String, String> payload) {
        // Empaquetamos los datos básicos en un DTO
        PostulanteDTO dto = PostulanteDTO.builder()
                .dni(payload.get("dni"))
                .nombres(payload.get("nombres"))
                .apellidos(payload.get("apellidos"))
                .correo(payload.get("correo"))
                .build();
        
        String especialidad = payload.get("especialidad");
        
        DocenteDTO nuevoDocente = docenteService.registrarDocente(dto, especialidad);
        return new ResponseEntity<>(nuevoDocente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DocenteDTO>> listarTodos() {
        return ResponseEntity.ok(docenteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(docenteService.obtenerPorId(id));
    }

    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<DocenteDTO> obtenerPerfilPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(docenteService.obtenerPerfilPorUsuario(usuarioId));
    }
}