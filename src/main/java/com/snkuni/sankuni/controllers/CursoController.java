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

    // Esta es la ruta que estaba causando el error
    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    @GetMapping("/carrera/{idCarrera}")
    public ResponseEntity<List<CursoDTO>> listarCursosPorCarrera(@PathVariable("idCarrera") Long idCarrera) {
        return ResponseEntity.ok(cursoService.listarPorCarrera(idCarrera));
    }

    @PostMapping
    public ResponseEntity<CursoDTO> crearCurso(@Valid @RequestBody CursoDTO dto) {
        return new ResponseEntity<>(cursoService.crearCurso(dto), HttpStatus.CREATED);
    }
}