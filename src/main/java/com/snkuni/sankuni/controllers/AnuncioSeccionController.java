package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.AnuncioSeccionDTO;
import com.snkuni.sankuni.services.AnuncioSeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/anuncios")
@RequiredArgsConstructor
public class AnuncioSeccionController {

    private final AnuncioSeccionService anuncioService;

    @PostMapping
    public ResponseEntity<AnuncioSeccionDTO> publicar(@RequestBody AnuncioSeccionDTO dto) {
        return new ResponseEntity<>(anuncioService.publicar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/seccion/{idSeccion}")
    public ResponseEntity<List<AnuncioSeccionDTO>> listarPorSeccion(@PathVariable Long idSeccion) {
        return ResponseEntity.ok(anuncioService.listarPorSeccion(idSeccion));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<AnuncioSeccionDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(anuncioService.listarPorAlumno(alumnoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        anuncioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
