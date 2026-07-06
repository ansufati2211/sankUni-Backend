package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.ModuloCursoDTO;
import com.snkuni.sankuni.services.ModuloCursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modulos")
@RequiredArgsConstructor
public class ModuloCursoController {

    private final ModuloCursoService moduloService;

    @PostMapping
    public ResponseEntity<ModuloCursoDTO> crear(@Valid @RequestBody ModuloCursoDTO dto) {
        return new ResponseEntity<>(moduloService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<ModuloCursoDTO>> listarPorCurso(@PathVariable Long idCurso) {
        return ResponseEntity.ok(moduloService.listarPorCurso(idCurso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuloCursoDTO> editar(@PathVariable Long id, @Valid @RequestBody ModuloCursoDTO dto) {
        return ResponseEntity.ok(moduloService.editar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        moduloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
