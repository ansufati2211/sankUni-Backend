package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.CursoDTO;
import com.snkuni.sankuni.services.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    public ResponseEntity<CursoDTO> crearCurso(@Valid @RequestBody CursoDTO dto) {
        // El @Valid asegura que los campos obligatorios del DTO estén presentes
        CursoDTO nuevo = cursoService.crearCurso(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }
}