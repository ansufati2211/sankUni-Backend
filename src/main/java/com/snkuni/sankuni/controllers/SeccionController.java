package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.SeccionDTO;
import com.snkuni.sankuni.services.SeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secciones")
@RequiredArgsConstructor
public class SeccionController {

    private final SeccionService seccionService;

    @GetMapping
    public ResponseEntity<List<SeccionDTO>> listarTodas() {
        return ResponseEntity.ok(seccionService.listarTodas());
    }

    @GetMapping("/ciclo/{ciclo}")
    public ResponseEntity<List<SeccionDTO>> listarPorCiclo(@PathVariable("ciclo") String ciclo) {
        return ResponseEntity.ok(seccionService.listarPorCiclo(ciclo));
    }

    // NUEVO: Mis Cursos (Docente)
    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<List<SeccionDTO>> listarPorDocente(@PathVariable Long idDocente) {
        return ResponseEntity.ok(seccionService.listarPorDocente(idDocente));
    }

    // NUEVO: Aperturar Curso (Coordinador)
    @PostMapping
    public ResponseEntity<SeccionDTO> crearSeccion(@RequestBody SeccionDTO dto) {
        return new ResponseEntity<>(seccionService.crearSeccion(dto), HttpStatus.CREATED);
    }
    @GetMapping("/docente/{idDocente}/dia/{diaSemana}")
    public ResponseEntity<List<SeccionDTO>> listarPorDocenteYDia(
            @PathVariable Long idDocente, @PathVariable Integer diaSemana) {
        return ResponseEntity.ok(seccionService.listarPorDocenteYDia(idDocente, diaSemana));
    }

    // NUEVO: Editar curso programado (Coordinador)
    @PutMapping("/{id}")
    public ResponseEntity<SeccionDTO> editarSeccion(
            @PathVariable Long id, @RequestBody SeccionDTO dto) {
        return ResponseEntity.ok(seccionService.editarSeccion(id, dto));
    }
}