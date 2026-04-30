package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.MaterialClaseDTO;
import com.snkuni.sankuni.services.MaterialClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materiales")
@RequiredArgsConstructor
public class MaterialClaseController {

    private final MaterialClaseService materialService;

    // POST /api/v1/materiales -> Docente sube un PDF o link
    @PostMapping
    public ResponseEntity<MaterialClaseDTO> subirMaterial(@RequestBody MaterialClaseDTO dto) {
        return new ResponseEntity<>(materialService.subirMaterial(dto), HttpStatus.CREATED);
    }

    // GET /api/v1/materiales/seccion/1 -> Alumno o Docente ven los materiales
    @GetMapping("/seccion/{idSeccion}")
    public ResponseEntity<List<MaterialClaseDTO>> listarPorSeccion(@PathVariable Long idSeccion) {
        return ResponseEntity.ok(materialService.listarPorSeccion(idSeccion));
    }
}