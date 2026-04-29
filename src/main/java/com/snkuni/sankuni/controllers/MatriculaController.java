package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.ApiResponseDTO;
import com.snkuni.sankuni.dtos.MatriculaRequestDTO;
import com.snkuni.sankuni.services.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping("/automatricula")
    public ResponseEntity<ApiResponseDTO> registrarMatricula(@Valid @RequestBody MatriculaRequestDTO request) {
        Long idMatricula = matriculaService.procesarAutomatricula(request);
        return new ResponseEntity<>(new ApiResponseDTO(true, "Matrícula procesada con éxito", idMatricula), HttpStatus.CREATED);
    }
}