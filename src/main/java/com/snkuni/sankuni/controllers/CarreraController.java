package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.CarreraDTO;
import com.snkuni.sankuni.services.CarreraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService carreraService;

    @GetMapping
    public ResponseEntity<List<CarreraDTO>> listarCarreras() {
        return ResponseEntity.ok(carreraService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<CarreraDTO> crearCarrera(@Valid @RequestBody CarreraDTO dto) {
        return new ResponseEntity<>(carreraService.crearCarrera(dto), HttpStatus.CREATED);
    }
}