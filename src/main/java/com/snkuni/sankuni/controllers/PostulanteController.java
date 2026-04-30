package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.services.PostulanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/postulantes")
@RequiredArgsConstructor
public class PostulanteController {

    private final PostulanteService postulanteService;

    // POST /api/v1/postulantes -> Registro desde admision.html
    @PostMapping
    public ResponseEntity<PostulanteDTO> registrar(@RequestBody PostulanteDTO dto) {
        return new ResponseEntity<>(postulanteService.registrar(dto), HttpStatus.CREATED);
    }

    // GET /api/v1/postulantes/pendientes -> Para que el coordinador los vea
    @GetMapping("/pendientes")
    public ResponseEntity<List<PostulanteDTO>> listarPendientes() {
        return ResponseEntity.ok(postulanteService.listarPendientes());
    }

    // POST /api/v1/postulantes/1/aprobar -> Botón de "Aprobar" del coordinador
    @PostMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobarPostulante(@PathVariable Long id) {
        return ResponseEntity.ok(postulanteService.aprobarPostulante(id));
    }
}