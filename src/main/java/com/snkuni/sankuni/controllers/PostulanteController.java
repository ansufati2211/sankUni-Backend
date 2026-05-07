
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

    @PostMapping
    public ResponseEntity<PostulanteDTO> registrar(@RequestBody PostulanteDTO dto) {
        return new ResponseEntity<>(postulanteService.registrar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PostulanteDTO>> listarPendientes() {
        return ResponseEntity.ok(postulanteService.listarPendientes());
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobarPostulante(@PathVariable Long id) {
        return ResponseEntity.ok(postulanteService.aprobarPostulante(id));
    }
}